package cz.zcu.students.kiwi.anlp.markov;

import cz.zcu.prazak.anlp.a02.LanguageModel;

public abstract class MarkovModel implements IMarkovModel {

	protected LanguageModel tagsModel, tagWordsModel;
	
	public MarkovModel(LanguageModel tagsModel, LanguageModel tagWordsModel) {
		this.tagsModel = tagsModel;
		this.tagWordsModel = tagWordsModel;
	}

}
