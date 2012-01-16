import scala.util.Random;
import scala.collection.mutable.ArrayBuffer;
class TwoSpin {
  private val N = 100000;
  private val p = 0.5;
  private val pp = 1.0 - p;
  private val rnd = new Random();
  private val ss = new ArrayBuffer[ArrayBuffer[Double]];
  private val temp = 1.01
  private val ensembles = new ArrayBuffer[ArrayBuffer[Double]];
  private val pas = new ArrayBuffer[Double];

  (0 until 2).foreach {
    (s) => {
      ss += new ArrayBuffer[Double];
      (0 until N).foreach { 
        (n) => {
          val r = rnd.nextDouble();
          ss(s) += Math.round(r + (p - 0.5)) * 2 -1;
        }
      }
    }
  }

  def e(s1: Double, s2: Double): Double = {
    val J = 1.0;
    val h1 = 0.5;
    val h2 = 0.1;
    return -J * s1 * s2 - h1 * s1 - h2 * s2;
  }

  def p(de: Double, temp: Double): Double = return Math.exp(-de/temp);

  def calcEnsemble(ss :ArrayBuffer[ArrayBuffer[Double]]) :ArrayBuffer[Double] = {
    val result = new ArrayBuffer[Double];
    (0 until 4).foreach { (i) => result += 0 };
    (0 until N).foreach {
      (n) => {
        val s1 = ss(0)(n);
        val s2 = ss(1)(n);
        val index = (Math.abs(s1 -1) + Math.abs(if(s2 == 1) 0 else 1) % 2).asInstanceOf[Int];
        result(index) += 1.0/N;
      }
    }
    return result;
  }

  def calcEnsembleAverage(): ArrayBuffer[Double] = {
    val averages = new ArrayBuffer[Double];
    ensembles.foreach {
      (ensemble) => {
        val average = e(1, 1) * ensemble(0) + e(1, -1) * ensemble(1) + e(-1, 1) * ensemble(2) + e(-1, -1) * ensemble(3);
        averages += average;
      }
    }
    return averages;
  }

  def calcLongTimeAverage(): ArrayBuffer[Double] = {
    return pas;
  }

  def run(max: Int): Unit = {
    (0 until max).foreach {
      (t) => {
        var esSum = 0.0;
        (0 until N).foreach {
          (n) => {
            val s = (Math.floor(rnd.nextDouble() * 2)).asInstanceOf[Int];
            val es = e(ss(0)(n), ss(1)(n));
            esSum += es;
            var esd = 0.0;
            if (s == 0) {
              esd = e(ss(0)(n) * -1, ss(1)(n));
            } else {
              esd = e(ss(0)(n), ss(1)(n) * -1);
            }
            val de = esd - es;
            val r = rnd.nextDouble();
            if (de < 0 || r < p(de, temp) ) {
              ss(s)(n) *= -1;
            }
          }
        }
        val ensemble = calcEnsemble(ss);
        ensembles += ensemble
        val pa = 1/temp * esSum * 1/N;
        pas += pa;
      }
    }
  }
}

object TwoSpinSimulator extends Application {
  val ts = new TwoSpin
  ts.run(500)
  var index = 0;
  val ensembles = ts.calcEnsembleAverage();
  val lt = ts.calcLongTimeAverage();
  (0 until ensembles.length).foreach {
    (index) => println(index + " " + ensembles(index) + " " + lt(index));
  }
}

TwoSpinSimulator main null
