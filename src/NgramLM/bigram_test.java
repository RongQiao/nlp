package NgramLM;

import java.util.List;

public class bigram_test {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("error: files not given.");
		}
		else {
			TrainingResultData trd = new TrainingResultData();
			trd.learnFromResultFile("LM.txt");
			
			String fileNameTest = "hw2_test.txt";
			DataFile df = new DataFile(fileNameTest);
			List<String> words = df.getWords();
			List<String> pairs = df.getPairs();
			//List<String> sentences = df.getSentences();
			//TestData td = new TestData(sentences);
			TestData td = new TestData(pairs, words.size());
			double ppl = td.test(trd);
			System.out.println(ppl);
		}
	}

}
