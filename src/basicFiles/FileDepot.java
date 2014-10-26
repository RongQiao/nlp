package basicFiles;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

//to make it simple, it's just for single directory
public class FileDepot {
	List<String> fileNames;
	int foldIndex[];
	
	public FileDepot() {
		fileNames = new ArrayList<String>();
	}

	public void getAllFilesName(String dirName) {
		fileNames.clear();
		try {
		    Path startPath = Paths.get(dirName);
		    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
		        @Override
		        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		            fileNames.add(file.toString());    
		            return FileVisitResult.CONTINUE;
		        }

		        @Override
		        public FileVisitResult visitFileFailed(Path file, IOException e) {
		            return FileVisitResult.CONTINUE;
		        }
		    });
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	public int getFilesCount() {
		return fileNames.size();
	}

	public void splitFold(int foldCnt) {
		foldIndex = new int[foldCnt];
		int fileCntPerFold = getFilesCount()/foldCnt;
		foldIndex[0] = 0;
		for (int i = 1; i < foldCnt; i++) {
			foldIndex[i] = foldIndex[i-1] + fileCntPerFold;
		}
		int additional = getFilesCount()%foldCnt;
		if (additional != 0) {
			foldIndex[foldCnt-1] += additional;
		}
	}	

	private int getFoldCount() {
		return foldIndex.length;
	}

	//input index begin from 0
	public List<String> getFold(int index) {
		int fromIndex = foldIndex[index];
		int toIndex = 0;
		if (index < getFoldCount() - 1) {
			toIndex = foldIndex[index+1];
		}
		else {
			toIndex = fileNames.size();
		}
		return fileNames.subList(fromIndex, toIndex);
	}

	public List<String> getFoldExcluse(int index) {
		List<String> total = new ArrayList<String>();
		for (int i = 0; i < getFoldCount(); i++) {
			if (i != index) {
				total.addAll(getFold(i));
			}
		}
		return total;
	}


}
