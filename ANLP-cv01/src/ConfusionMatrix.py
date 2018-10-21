UNI = 4

class ConfusionMatrix:
    def __init__(self, word = None):
        self.word = word
        self.values = [0, 0, 0, 0]
    
    def addMatrix(self, other, unmarkedNegatives = True):
        for i in range(len(self.values)):
            if not unmarkedNegatives and i == UNI:
                continue

            self.values[i] += other.values[i]

    def totalValues(self, unmarkedNegatives = True):
        sum = 0
        for i, count in enumerate(self.values):
            if i != UNI or unmarkedNegatives:
                sum += count

        return sum

    @property
    def positiveHits(self):
        return self.values[0]
    @positiveHits.setter
    def positiveHits(self, value):
        self.values[0] = value

    @property
    def positiveMisses(self):
        return self.values[1]
    @positiveMisses.setter
    def positiveMisses(self, value):
        self.values[1] = value


    @property
    def negativeHits(self):
        return self.values[2]
    @negativeHits.setter
    def negativeHits(self, value):
        self.values[2] = value

    @property
    def negativeMisses(self):
        return self.values[3]
    @negativeMisses.setter
    def negativeMisses(self, value):
        self.values[3] = value

    @property
    def allHits(self):
        return self.positiveHits + self.negativeHits

    @property
    def allPositive(self):
        return self.positiveHits + self.positiveMisses
    
    @property
    def allNegative(self):
        return self.negativeHits + self.negativeMisses

