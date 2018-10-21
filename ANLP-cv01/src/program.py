from pathlib import Path
import json

from MorphDictionary import MorphDictionary
from MorphCompare import MorphCompare

class MorphSettings:
    morphTagsFile = "./data/train.m"
    testFile = "./data/test.txt"
    outputTagged = "./data/test-tagged.dict"
    referenceTagged = "./data/morph.dict"


def morphologicalTag(tagsFile, testFile):
    print("Loading morph dictionary...")
    tagDictionary = MorphDictionary.loadFromFile(tagsFile)
    print(f"Loaded morph dictionary with {len(tagDictionary)} words")

    input = loadInputFile(testFile)
    print(f"Loaded input with {len(input)} words")

    taggedInput = tagWords(input, tagDictionary)
    print(f"Tagged {len(taggedInput)} words")

    return taggedInput  


def loadInputFile(path):
    with open(path, encoding="utf8") as inputFile:
        lines = inputFile.readlines()
        strippedLines = [line.strip() for line in lines]

        return strippedLines

def tagWords(words, dictionary):
    dict = MorphDictionary()
    
    for word in words:
        entry = dictionary.get(word)
        if entry == None:
            continue
            
        dict.putEntry(entry)

    return dict




def main():
    settings = MorphSettings()

    taggedFile = Path(settings.outputTagged)

    if taggedFile.is_file():
        print (f"Loading from file {settings.outputTagged}")
        tagged = MorphDictionary.loadFormatted(settings.outputTagged)
    else:
        print("Computing morphological tags")
        tagged = morphologicalTag(settings.morphTagsFile, settings.testFile)
        print(f"Saving to file {settings.outputTagged}")
        tagged.saveFormatted(settings.outputTagged)
    
    if not tagged:
        raise Exception("Morphological tag output is empty")

    print(f"Loading reference from file {settings.referenceTagged}")
    reference = MorphDictionary.loadFormatted(settings.referenceTagged)
    print(f"Loaded reference dictionary with {len(reference)} entries")

    comparison = MorphCompare.compare(reference, tagged)
    
    serialized = json.dumps(comparison, indent=4, sort_keys=True)
    print(serialized)
    

main()