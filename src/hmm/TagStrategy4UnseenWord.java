package hmm;

import java.util.Map.Entry;
import java.util.Set;

import NgramLM.TrainingResultData;

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
		double Pt = trainResult.tagMap.getProbability(tag);
		//because w is unseen, we don't have P(w) and P(t|w) in training result
		double Pw = calculateProbUnseenWord(word);		
		double Pt4w = calculateProbTagGivenWord(tag, word);
		Pw4t = Pw * Pt4w / Pt;
		return Pw4t;
	}

	private double calculateProbUnseenWord(String word) {
		// just give a small number, choose it from train result whose count == 1
		if (Double.compare(probForUnseenWord, Double.MIN_VALUE) < 0) {
			Set<Entry<String, WordTagStatisticData>> entries = trainResult.wordMap.entrySet();
			for (Entry<String, WordTagStatisticData> en: entries) {
				WordTagStatisticData sd = en.getValue();
				if (sd.getCount() == 1) {
					probForUnseenWord = sd.getProbability();
					break;
				}
			}
		}
		return probForUnseenWord;
	}

	private double calculateProbTagGivenWord(String tag, String word) {
		// try a simple way: use unigram probability of tag
		double prob = 0.0;
		prob = trainResult.tagMap.getProbability(tag);
		return prob;
	}

}
