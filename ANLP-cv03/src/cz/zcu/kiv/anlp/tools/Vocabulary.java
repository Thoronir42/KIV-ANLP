package cz.zcu.kiv.anlp.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Vocabulary implements Serializable {

	private Map<String, Integer> vocabulary;
	private Map<String, Integer> wordToKeyMap;
	private List<String> words = new ArrayList<>();
	private int counter = 0;
	public static final String SENTENCE = "<s>";
	public static final String END = "<e>";
	private int wordCount;
	
	
	public Vocabulary() {
		this.vocabulary = new HashMap<>();
		this.wordToKeyMap = new HashMap<>();
		this.add(SENTENCE);
		this.add(END);
		add(SENTENCE);
		add(END);
	}

	
	public void add(String word) {
		if (!wordToKeyMap.containsKey(word)) {
			wordToKeyMap.put(word, counter);
			vocabulary.put(word, 1);
			counter++;
			words.add(word);
		}
		else
		{
			vocabulary.put(word, vocabulary.get(word) + 1);
		}
	}
	
	public String getWordByKey(int key) {
		if (key < 0 || key >= vocabulary.size()) throw new IllegalArgumentException("word key ("+key+") must range between 0 and "+(vocabulary.size()-1));
		return words.get(key);
	}
	
	public String[] getWordByKey(int key[]) {
		String[] words = new String[key.length];
		for (int i=0; i<words.length; i++) words[i] = getWordByKey(key[i]);
		return words;
	}
	
	public int getWordKey(String word) {
		Integer key = this.wordToKeyMap.get(word);
		return (key != null ? key : -1);
	}
	
	public int[] getWordKey(String[] words) {
		int[] keys = new int[words.length];
		for (int i=0; i<words.length; i++) keys[i] = getWordKey(words[i]);
		return keys;
	}
	
	public int size() {
		return this.vocabulary.size();
	}

	public List<String> getVocabulary() {
		return words;
	}

	public Map<String, Integer> getWeitedVocabulary()
	{
		return vocabulary;
	}

	public void prune(int count)
	{
		wordCount = count;
		words = vocabulary.entrySet().stream().limit(count).map(Map.Entry::getKey).collect(Collectors.toList());
		for(int i = 0; i < words.size(); i++)
		{
			wordToKeyMap.put(words.get(i), i);
		}
	}
}
