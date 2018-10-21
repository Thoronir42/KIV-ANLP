import unittest
from MorphCompare import MorphCompare, ConfusionMatrix

class MorphCompareTest(unittest.TestCase):

    def testConfusionMatrix(self):
        expectedTags = ['1', '3', '4', '5', '7']
        actualTags = ['1', '2', '3', '6']

        cm = MorphCompare._confusionMatrix("word", expectedTags, actualTags, 20)

        self.assertEqual(2, cm.positiveHits, "Marked positive")
        self.assertEqual(2, cm.negativeHits, "Marked negative")
        self.assertEqual(3, cm.positiveMisses, "Unmarked positive")
        self.assertEqual(13, cm.negativeMisses, "Unmarked negative")