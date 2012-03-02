/* Donghyo Min; gajok@cs.washington.edu
 * 
 */

import java.io.*;
import java.util.*;

/*
 * V<-ExtractVocabulary(D): Set<String> vocabulary
 * N<-CountDocs(D) : trainings_count
 * each class in Classes is processed in EachFile class
 */
public class Train {
	public Set<EachFile> trainings;				// set for each classes.
	public Set<EachFile> trainings_real;		// set for each classes. used as a buffer.
	public Set<String> opinion_values;			// prediction
	public Set<String> vocabulary;				// all the words from training files
	public int trainings_count; 				// the number of all files we use for training
	public boolean cross_validation;			// true: do cross validation; false: predict test files without performing cross validation
	Map<String, ArrayList<File>> test_files;	// cross validation case: will be used in testing. key: class, value: files to be used in testing.
	public int validation_count;				// indicate how many validations we've done.
	
	public Train() throws IOException{
		trainings = new HashSet<EachFile>();
		trainings_real = new HashSet<EachFile>();
		vocabulary = new HashSet<String>();
		opinion_values = new HashSet<String>();
		trainings_count = 0;	
		this.cross_validation = false;
	}
	
	public Train(boolean cross_validation, int validation_count) throws IOException{
		trainings = new HashSet<EachFile>();
		trainings_real = new HashSet<EachFile>();
		vocabulary = new HashSet<String>();
		opinion_values = new HashSet<String>();
		trainings_count = 0;	
		this.cross_validation = cross_validation;
		test_files = new HashMap<String, ArrayList<File>>(); 
		this.validation_count = validation_count;
	}
	
	/*
	 * Read files in all class
	 */
	public void buildvoca(File dir){	
		if (dir.isDirectory()) {
			for (File subfile : dir.listFiles()) {
				buildvoca(subfile);
			}
		} else if(dir.isFile()){
			try {
				if(!cross_validation){
					trainings_count++;
				}
				Scanner scanner = new Scanner(dir);
				String readLine;

				// Add each word in the file to the set 'vocabulary'
				while(scanner.hasNextLine()) {
					readLine = scanner.nextLine();
					Scanner lineScanner = new Scanner(readLine);
					String word="";
					while(lineScanner.hasNext()){
						word = lineScanner.next(); 
						if(!vocabulary.contains(word)){
							vocabulary.add(word);
						}
					}
				}
				scanner.close();

			} catch (IOException e) {
				System.err.println("Error :" + dir + " " + e);
				System.exit(1);
			}
		}
	}
	
	
	public void learnNaiveBayesText(String train) throws IOException{
		File train_dir = new File(train);	
		
		// check all possible kinds of classes, then 
		// call 'buildvoca' to train, using files in each class.
		for(File trainingDirectories : train_dir.listFiles()){
			opinion_values.add(trainingDirectories.getName());
			
			// do building voca only when it's not doing cross validation
			if(!cross_validation){
				buildvoca(trainingDirectories);
			}
		}

		// For each class, construct a 'EachFile' class.
		// Then, set it with relevant information.
		for(File subset : train_dir.listFiles()){
			EachFile newSet;
			
			// when its doing cross validation, we get the list of the files used for training.
			if(cross_validation){
				newSet = new EachFile(true, validation_count);
				
				// ArrayList<File> file_voca: files used for training.
				ArrayList<File> file_voca = newSet.readDirCross(subset);
				for(File voca: file_voca){
					trainings_count++;
					buildvoca(voca);
				}
			} else {
				newSet = new EachFile(false);
				newSet.readDir(subset);
			}
			
			trainings_real.add(newSet);
			if(cross_validation){
				// will be used in testing
				test_files.put(newSet.Label, newSet.test_file);
			}
		}
		
		// now we have vocabulary set from all the docs.
		// also, we have total number of the docs we used for training.
		// we can calculate conditional probability and and prior.
		for(EachFile classes: trainings_real){
			classes.calculateProb(vocabulary);
			classes.setpv(trainings_count);
			trainings.add(classes);
		}
		
	}
}
