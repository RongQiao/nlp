package hmm;

import static org.junit.Assert.assertTrue;
import basicFiles.TextFile;

public class HMM {
	private TagTrainingResult trainResult = null;

	public static void main(String[] args) {
		HMM hmm = new HMM();
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("-train")) {
				hmm.hmmTrain(args[1], args[3]);
			}
			else {
				hmm.hmmEvaluate(args[1], args[3]);
			}
		}
		else if (args.length == 6) {
			hmm.hmmTest(args[1], args[3], args[5]);
		}
		else {
			printError();
		}
	}
	
	private TagTrainingResult getTrainResult() {
		return getTrainResult("hw3.txt");
	}
	
	private TagTrainingResult getTrainResult(String modelFile) {
		if (trainResult == null) {
			trainResult = new TagTrainingResult();
			trainResult.learnAllTrainingResult(modelFile);
		}
		return trainResult;
	}
	
	private void hmmTest(String testFile, String modelFile, String tagedFile) {				
		TagTest tt = new TagTest();
		TagTrainingResult ttr = getTrainResult(modelFile);
		tt.test(ttr, testFile,tagedFile);
	}


	private void hmmEvaluate(String refFile, String tagedFile) {
		TagTestEvaluate tte = new TagTestEvaluate();
		TagTrainingResult ttr = getTrainResult();
		ttr.learnAllTrainingResult();
		tte.setTrainingResult(ttr);
		tte.evaluate(tagedFile, refFile);
		tte.outputAccuracy();
	}

	private void hmmTrain(String trainFile, String modelFile) {	
		TagTrainFile tdf = new TagTrainFile(trainFile);
		TagTraining tt = new TagTraining(tdf);
		tt.trainWithoutOutput();
		tt.outputModel(modelFile);
	}

	private static void printError() {
		System.out.println("error: the given information isn't enough.");
	}

}
