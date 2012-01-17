import math
import random
import itertools
from PIL import Image

class TwoSpin(object):
    L = (100, 100)
    N = reduce(lambda x, y: x * y, L)
    P = 0.7
    J = 1.0
    h1 = 0.5
    h2 = 0.1
    T = 1.01

    def __init__(self, tmax):
        self.tmax = tmax
        self._matrix = [[random.choice((-1, 1)) for y in xrange(self.L[1])] for x in xrange(self.L[0])]

    def e(self, s1, s2):
        return -self.J * s1 * s2 - self.h1 * s1 - self.h2 * s2

    def _p(self, de, temp):
        return math.exp(-de / temp)

    def run(self):
        for t in xrange(self.tmax):
            for x, y in itertools.product(*map(lambda x: xrange(x), self.L)):
                p = (x, y)
                for np in self._get_neighbor(p):
                    self._metro(p, np)
        print "finish"

    def show_image(self):
        image = Image.new("L", self.L)
        for x, y in itertools.product(*map(lambda x: xrange(x), self.L)):
            p = (self._get_magnet((x, y)) + 1) * 255 / 2
            image.putpixel((x, y), p)
        image = image.resize(map(lambda x: x * 4, self.L))
        image.show()

    def _metro(self, p1, p2):
        ps = (p1, p2)
        m1 = self._get_magnet(p1)
        m2 = self._get_magnet(p2)
        s = random.choice(xrange(2))
        es = self.e(m1, m2)
        if s == 0:
            esd = self.e(m1 * -1, m2)
        else:
            esd = self.e(m1, m2 * -1)
        de = esd - es
        r = random.random()
        if de < 0 or r < self._p(de, self.T):
            self._matrix[ps[s][0]][ps[s][1]] *= -1

    def _get_neighbor(self, p):
        x, y = p
        return (((x + 1) % self.L[0], y), ((x - 1) % self.L[0], y), (x, (y + 1) % self.L[1]), (x, (y - 1) % self.L[1]))

    def _get_magnet(self, p):
        x, y = p
        return self._matrix[x][y]

if __name__ == '__main__':
    twospin = TwoSpin(100)
    twospin.run()
    twospin.show_image()
