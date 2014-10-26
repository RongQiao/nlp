package hmm;

import java.util.Map.Entry;
import java.util.Set;

import NgramLM.NgramTrainingResult;

public class TagStrategy4UnseenWord {
	TagTrainingResult trainResult;
	double probForUnseenWord = 0.0;

	public void setTrainResult(TagTrainingResult trainResult) {
		this.trainResult = trainResult;
	}

	/*
	 * P(w|t) =P(w)*P(t|w) / P(t).
	 */
	public double giveProbToWordTagPair(String word, String tag) {
		double Pw4t = 0.0;
		double Pt4w = calculateProbTagGivenWord(tag, word);
		double Pt = trainResult.tagMap.getProbability(tag);
		//because w is unseen, we don't have P(w) and P(t|w) in training result
		double Pw = calculateProbUnseenWord(word);		
		Pw4t = Pw * Pt4w / Pt;
		return Pw4t;
	}

	private double calculateProbUnseenWord(String word) {
		// just give a small number, choose it from train result the min prob		
		if (Double.compare(probForUnseenWord, Double.MIN_VALUE) < 0) {	
			probForUnseenWord = trainResult.wordMap.getMinProbability();
			
		}
		return probForUnseenWord;
	}

	// get the possible tag of word based word itself
	private double calculateProbTagGivenWord(String tag, String word) {
		double prob = 0.0;
		boolean solved = false;
		TTag tg = new TTag(tag); 
		if (isNumber(word)) {
			prob = tg.probIsNum();
			if (Double.compare(prob, Double.MIN_VALUE) > 0) {				
				solved = true;
			}
		}
		//not solved via word
		if (!solved) {
			prob = calculateProbTagGivenWord(tag);
		}
		return prob;
	}
	
	private boolean isNumber(String word) {
		//get rid of ','
		word = word.replace(",", "");
		boolean ret = false;
		ret = isInteger(word);
		if (!ret) {
			ret = isFloat(word);
		}
		return ret;
	}

	private boolean isFloat(String word) {
		try {
			Double.parseDouble(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isInteger(String word) {
		try {
			Integer.parseInt(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// try a simple way: use unigram probability of tag
	private double calculateProbTagGivenWord(String tag) {		
		double prob = 0.0;
		prob = trainResult.tagMap.getProbability(tag);
		return prob;
	}

}
