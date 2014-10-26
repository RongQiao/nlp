package NgramLM;

import java.util.List;

import basicFiles.DataFile;

public class bigram_train {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("error: files not given.");
		}
		else {
			String fileName = args[1];
			DataFile df = new DataFile(fileName);
			List<String> words = df.getWords();
			List<String> pairs = df.getPairs();
			NgramTraining td = new NgramTraining(words, pairs);
			td.OUT_FILENAME = args[3];
			td.training();
			System.out.println("training finished");
		}
	}

}
