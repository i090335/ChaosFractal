import scala.util.Random;
import scala.collection.mutable.ArrayBuffer;
class TwoSpin {
  private val N = 100000;
  private val p = 0.5;
  private val pp = 1.0 - p;
  private val rnd = new Random();
  private val ss = new ArrayBuffer[ArrayBuffer[Double]];
  private var temp = 1.01

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

  def calcEnsembleAverage(ss :ArrayBuffer[ArrayBuffer[Double]]) :ArrayBuffer[Double] = {
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

  def run(max: Int): Unit = {
    (0 until max).foreach {
      (t) => {
        (0 until N).foreach {
          (n) => {
            val s = (Math.floor(rnd.nextDouble() * 2)).asInstanceOf[Int];
            val es = e(ss(0)(n), ss(1)(n));
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
        val ensemble = calcEnsembleAverage(ss);
        println(t + " " + ensemble.mkString(" "));
      }
    }
  }
}

object TwoSpinSimulator extends Application {
  val ts = new TwoSpin
  ts.run(500)
}

TwoSpinSimulator main null
