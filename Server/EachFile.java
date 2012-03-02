/* 
 * Donghyo Min; gajok@cs.washington.edu
 */

import java.io.*;
import java.util.*;

/*
 * Java Class for Each Class
 *  
 * for each c in C
 * do N_c <- CountDocsInClass: size
 * 		prior[c] <- N_c/N: pv
 * 		text_c <- ConcatenateTextOfAllDocsInClass(D,c)
 * 		for each word in the whole set of words
 * 		do T_ct <- CountTokensOfTerm
 * 		for each word in the whole set of words
 * 		do conditional prob[t][c] <- (T_ct + 1)/sum_t'(T_ct' + 1) Laplace smoothing.
 */
public class EachFile {
	// pv = the number of files in a class divide by the number of files from all the files from all the class
	public double pv;

	public int total_num_occur;						// total number of words occurrence in this class	
	public int size;								// num of Files in the class 								
	public Map<String, Integer> counting;			// map a key(word) to the number of occurrence of each word in the class 
	public Map<String, Double> probability_word;	// mapping for a probability of each word
	public String Label;
	
	private boolean cross_valid;					// indicate if we use cross_validation or not. true for using it.
	private int validation_count;					// indicate how many validations we have done.  
	public ArrayList<File> test_file;				// when we do cross_validation, used for testing.
	
	public EachFile(boolean cross_valid) {
		total_num_occur = 0;
		size = 0;
		pv = 0.0;
		counting = new HashMap<String, Integer>();
		probability_word = new HashMap<String, Double>();
		Label = "";
		this.cross_valid = cross_valid;
		this.validation_count = 0;
	}
	
	public EachFile(boolean cross_valid, int validation_count) {
		total_num_occur = 0;
		size = 0;
		pv = 0.0;
		counting = new HashMap<String, Integer>();
		probability_word = new HashMap<String, Double>();
		Label = "";
		this.cross_valid = cross_valid;
		this.validation_count = validation_count;
		test_file = new ArrayList<File>();
	}
	
	/*
	 * param examplesCount: the number of files from all training samples.
	 * set pv(prior) value.
	 */
	public void setpv(int examplesCount){
		int examples = examplesCount;
		this.pv = (double)size / examplesCount;
		System.out.println();
	}

	public void setLabel(String Label){
		this.Label = Label;
	}

	/*
	 * Reading a file, build a map for counting of each word in the file.
	 */
	public void readFile(File file) throws IOException{
		String readLine;
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			readLine = scanner.nextLine();
			Scanner lineScanner = new Scanner(readLine);
			String word="";
			while(lineScanner.hasNext()){
				word = lineScanner.next(); 
				total_num_occur++;
				if(counting.containsKey(word)){
					int count = counting.get(word);
					counting.put(word, count++);
				} else {
					counting.put(word, 1);
				}
			}
		}
		scanner.close();
	}

	/*
	 * Calculate conditional probability
	 * param vocabulary: A set for the all words in all docs.
	 */
	public void calculateProb(Set<String> vocabulary){
	//	for(String word:counting.keySet()){
		for(String word : vocabulary){
			int word_c = 0;
			if(this.counting.containsKey(word)){
				word_c = this.counting.get(word);	// T_ct: count of this token 
			}
			
			// Laplace smoothing is applied.
			// calculating conditional probability.
			double p = (word_c + 1) / (double)(size + vocabulary.size());
			this.probability_word.put(word, p);
		}
	}
	
	/* Going through all the files in the given directory.
	 * Call readFile to process a file.
	 * param dir: directory name
	 */
	public void readDir(File dir) throws IOException{
		setLabel(dir.getName());

		if (dir.isDirectory()) {
			for (File subdir : dir.listFiles()) {
				if (subdir.isDirectory()) {
					for (File subfile : subdir.listFiles()) {
						size++;
						readFile(subfile);
					}
				}
			}
		}
	}
	
	/* Going through all the files in the given directory.
	 * 
	 * However, this is used only for we are doing cross validation.
	 * We need to use only some portion(all except 10) of documents for each class while training.
	 * The files we are not using is going to be used for testing.
	 * We are going to store these files in the arraylist test_file, and then use it later.
	 * The files used for testing is going to be stored in arraylist file_voca, and then
	 * we are going to retunr file_voca.
	 * Call readFile to process a file.
	 * param dir: directory name
	 */
	public ArrayList<File> readDirCross(File dir) throws IOException{
		ArrayList<File> file_voca = new ArrayList<File>();
		setLabel(dir.getName());

		int count = 0;
		if (dir.isDirectory()) {
			for (File subdir : dir.listFiles()) {
				if (subdir.isDirectory()) {
					for (File subfile : subdir.listFiles()) {
						count++;
						if(count <= validation_count * 10 && count > (validation_count - 1) * 10){
							test_file.add(subfile);
						} else {
							file_voca.add(subfile);
							size++;
							readFile(subfile);
						}
					}
				}
			}
		}
		return file_voca;
	}
}
