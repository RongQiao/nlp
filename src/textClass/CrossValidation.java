package textClass;

import java.io.IOException;

public class CrossValidation {
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("error: files not given.");
		}
		else {
			String dirPos = args[1] + "/pos";
			String dirNeg = args[1] + "/neg";
			String modelFileName = args[3];
			
			double acSum = 0.0;
			for (int foldIndex = 0; foldIndex < 10; foldIndex++) {			
				TextClassTrain.trainMain(dirPos, dirNeg, foldIndex, modelFileName);
				acSum += TextClassify.testMain(dirPos, dirNeg, foldIndex, modelFileName);
			}
			System.out.println("average accuracy: " + acSum/10);
		}
	}
}
