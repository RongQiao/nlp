package NgramLM;

import java.util.List;

public class bigram_test {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("error: files not given.");
		}
		else {
			TrainingResultData trd = new TrainingResultData();
			trd.learnFromResultFile(args[3]);
			
			String fileNameTest = args[1];
			DataFile df = new DataFile(fileNameTest);
			List<String> words = df.getWords();
			List<String> pairs = df.getPairs();
			TestData td = new TestData(pairs, words.size());
			double ppl = td.test(trd);
			System.out.println(ppl);
		}
	}

}
