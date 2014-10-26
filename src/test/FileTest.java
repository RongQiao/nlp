package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import basicFiles.FileDepot;

public class FileTest {

	@Test
	public void getAllFilesName() {
		FileDepot fd = new FileDepot();
		fd.getAllFilesName("txt_sentoken/pos");
		int cnt = fd.getFilesCount();
		assertTrue(cnt == 1000);
	}
	
	@Test
	public void fileTest10folds() {
		FileDepot fd = new FileDepot();
		fd.getAllFilesName("txt_sentoken/pos");
		fd.splitFold(10);
		List<String> fns = fd.getFold(0);
		assertTrue(fns.size() == 100);
		assertTrue(fns.get(0).indexOf("000") > 0);
		assertTrue(fns.get(99).indexOf("099") > 0);
		System.out.println(fns.get(99));
	}

}
