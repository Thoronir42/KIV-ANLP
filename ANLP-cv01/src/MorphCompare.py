import bisect

from MorphDictionary import MorphDictionary
from ConfusionMatrix import ConfusionMatrix

"""Comparison class which calculates Accuracy, Precision, Recall and Specifity

    Might be optimized by using sorted collections
"""
class MorphCompare:

    @staticmethod
    def compare(reference, tested):
        print(f"Pairing {len(tested)} entries to reference containing {len(reference)} items")
        result = MorphCompare._filterPairs(reference, tested)

        message = f"Paired {len(result['pairs'])} words"
        if result["missed"]:
            message += f", missed {len(result['missed'])} words"
        print(message)
        
        print("Finding unique tags")
        uniqueTags = _getUniqueTags(reference, tested)
        uniqueTagsLen = len(uniqueTags)
        print(f"Found {uniqueTagsLen} unique tags")

        print("Computing confusion matrices")
        confusionMatrices = [MorphCompare._confusionMatrix(pair[0].word, pair[0].tags, pair[1].tags, uniqueTagsLen) for pair in result["pairs"]]

        print("Calculating confusion stats")
        return MorphCompare._computeConfusionStats(confusionMatrices)


    @staticmethod
    def _filterPairs(reference, tested):
        missed = []
        pairs = []

        for word, ref in tested.items():
            test = reference.get(word)
            if test == None:
                missed.append(ref)
            else:
                pairs.append([ref, test])
        
        return {'missed': missed, 'pairs': pairs}
    
    @staticmethod
    def _confusionMatrix(word, expectedTags, actualTags, totalTags = None):
        cm = ConfusionMatrix(word)

        expected = expectedTags.copy()

        falselyMarked = 0
        positivelyMarked = 0

        for tag in actualTags:
            try:
                index = expected.index(tag)
                positivelyMarked += 1
                expected.pop(index)
            except:
                falselyMarked += 1

        cm.positiveHits = positivelyMarked
        cm.negativeHits = falselyMarked
        cm.positiveMisses = len(expected)

        if totalTags != None:
            cm.negativeMisses = totalTags - cm.totalValues(False)

        return cm
    
    @staticmethod
    def _computeConfusionStats(confusionMatrices):
        matricesCount = len(confusionMatrices)
        totalMatrix = ConfusionMatrix()

        macroSumPrecision = 0
        macroSumRecall = 0

        for matrix in confusionMatrices:
            if not matrix.allHits or not matrix.allPositive:
                print(f"Wrong matrix values of {matrix.word}")
                continue
            
            totalMatrix.addMatrix(matrix)
            macroSumPrecision += matrix.positiveHits / matrix.allHits
            macroSumRecall += matrix.positiveHits / matrix.allPositive
        
        microAvg = {
            'precision': totalMatrix.positiveHits / totalMatrix.allHits,
            'recall': totalMatrix.positiveHits / totalMatrix.allPositive,
        }

        return {
            'standard': {
                'accuracy': (totalMatrix.positiveHits + totalMatrix.negativeMisses) / totalMatrix.totalValues(),
                'specificity': totalMatrix.negativeMisses / totalMatrix.allNegative,
                'fscore':  2 * totalMatrix.positiveHits / (2 * totalMatrix.positiveHits + totalMatrix.negativeHits + totalMatrix.negativeHits)
            },
            'micro': microAvg,
            'macro': {
                'precision': macroSumPrecision / matricesCount,
                'recall': macroSumRecall / matricesCount,
            }
        }



def _getUniqueTags(*entryLists: list):
    """
    :param entryLists MorphDictionary[]
    """
    uniqueTags = []

    # wow, this is on heck of a pyramid
    for entries in entryLists:
        for word, entry in entries.items():
            for tag in entry.tags:
                index = bisect.bisect(uniqueTags, tag)
                
                # if tag == 'AAFD7----1A----':
                #     print(f"{word} - {index}")

                ## index ends up after every instance of searched term
                if index > 0 and uniqueTags[index - 1] == tag:
                    continue
                
                uniqueTags.insert(index, tag)
    
    return uniqueTags