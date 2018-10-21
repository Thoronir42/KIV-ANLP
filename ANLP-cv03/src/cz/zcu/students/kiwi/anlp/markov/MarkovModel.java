package cz.zcu.students.kiwi.anlp.markov;

import cz.zcu.students.kiwi.anlp.model.LanguageModel;

public abstract class MarkovModel implements IMarkovModel {

	protected LanguageModel tagsModel, tagWordsModel;
	
	public MarkovModel(LanguageModel tagsModel, LanguageModel tagWordsModel) {
		this.tagsModel = tagsModel;
		this.tagWordsModel = tagWordsModel;
	}

}
