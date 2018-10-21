
class MorphDictionary:

    def __init__(self):
        self._dictionary = {}

    def put(self, word, tags):
        if isinstance(tags, str):
            tags = [tags]

        if word not in self._dictionary:
            self._dictionary[word] = MorphEntry(word, tags)
            return

        for tag in (tag for tag in tags if tag not in self._dictionary[word].tags):
            self._dictionary[word].tags.append(tag)

    def putEntry(self, entry):
        if entry.word not in self._dictionary:
            self._dictionary[entry.word] = entry

    def get(self, word):
        return self._dictionary.get(word, None)

    def items(self):
        return self._dictionary.items()

    def __len__(self):
        return len(self._dictionary)

    @staticmethod
    def loadFromFile(path):
        instance = MorphDictionary()

        with open(path, encoding="utf8") as f:
            for i,line in enumerate(f):
                parts = line.strip().split('|')
                if len(parts) != 2:
                    raise Exception(f"Karel on line {i} >:(")

                instance.put(parts[0], parts[1])

        return instance


    def saveFormatted(self, path):
        with open(path, 'w', encoding="utf8") as output:
            for word, entry in self.items():
                output.write(entry.word + "|" + "|".join(entry.tags) + "\n")

            print(f"Written {output.tell()/1024} kbytes to '{path}'")

    @staticmethod
    def loadFormatted(path):
        dict = MorphDictionary()

        with open(path, encoding="utf8") as inputFile:
            for line in inputFile:
                parts = line.strip().split('|')
                word = parts.pop(0)

                dict.putEntry(MorphEntry(word, parts))

        return dict


class MorphEntry:
    def __init__(self, word, tags):
        self.word = word
        self.tags = tags