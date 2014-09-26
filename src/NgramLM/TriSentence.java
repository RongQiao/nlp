package NgramLM;

public class TriSentence extends Sentence {
	public TriSentence(String line) {
		super(line);
	}

	public void putWords(String sOut[]) {
		String s = new String(this.content, 0, content.length);
		for (int i = 0; i < 2; i++) {
			int index = s.indexOf(this.getSeperator());
			sOut[i] = s.substring(0, index);
			s = s.substring(index+1, s.length());
		}
		sOut[2] = s.substring(0, s.length());
	}

}
