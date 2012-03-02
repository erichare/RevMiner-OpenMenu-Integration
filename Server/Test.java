/*
 * Donghyo Min; gajok@cs.washington.edu
 */

import java.io.*;
import java.util.*;

public class Test {
	public PrintStream output;

	public Test(String output_file) throws IOException{
		output = new PrintStream(new File(output_file));
	}
	
	public Test(){}

	public static String calculateMaxNB(HashSet<String> test_voc, Train newTrain){
		double max_vnb = -999999999.9;
		String test_opinion = "";
		
		for (EachFile trainingDoc : newTrain.trainings){		// for each class, do the job.
			String label = trainingDoc.Label;		
			Map<String, Double> probMap = trainingDoc.probability_word;
			Double vnb = Math.log(trainingDoc.pv);	// vnb(score[c]) = log prior[c]
			for(String word: test_voc){
				if(!probMap.containsKey(word)){
					Double prob = 1.0 / (probMap.size()+newTrain.vocabulary.size());
					vnb += Math.log(prob);
				}else{
					Double prob = trainingDoc.probability_word.get(word);
					vnb += Math.log(prob);
				}
			}
			if(vnb > max_vnb){
				max_vnb = vnb;
				test_opinion = label;
			}	
		}
		return test_opinion;
	}
	
	/*
	 * This is used for predict the class when it's Not doing cross validation.
	 * To get the prediction, it is use calculateMaxNB method.
	 * Files to predict should be located in a folder.
	 */
	public void classifyNaiveBayesText(String test, String output, Train newTrain) throws IOException{
		File test_dir = new File(test);
		for(File test_f:test_dir.listFiles()){		
			EachFile test_doc = new EachFile(false);
			
			// build a map each word to count.
			test_doc.readFile(test_f);
			
			HashSet<String> test_voc = new HashSet<String>();

			// for each word, if the word is in the vocabulary set we used when training,
			// then add that word.
			for(String ori_word: test_doc.counting.keySet()){
				if(newTrain.vocabulary.contains(ori_word)){
					test_voc.add(ori_word);
				}
			}
			
			// Using the set of the word we added into the test_voc, predict the class.
			String test_opinion= calculateMaxNB((HashSet<String>) test_voc, newTrain);
			String file_name = test_f.getName();
			this.output.println(file_name + "|" + test_opinion);
		}
	}

	/*
	 * This is used for predict the class when it's doing cross validation.
	 * To get the prediction, it is use calculateMaxNB method.
	 */
	public void classifyNaiveBayesTextCross(Map<String, ArrayList<File>> test, 
			PrintStream output, Train newTrain, int count) throws IOException{
		output.println();
		output.println("the following is " + count + "th validation result: precision/recall");

		Map<String, Integer> predict = new HashMap<String, Integer>();	// key: class, value: the number of the files which are guessed to be in the class
		Map<String, Integer> trueGuess = new HashMap<String, Integer>();// key: class, value: the number of correct guess
		for(String label: test.keySet()){
			String true_label = label;
			int num_file_in_dir = 0;
			int true_guess = 0;
			if(!predict.containsKey(true_label)){
				predict.put(true_label, 0);
			}
			if(!trueGuess.containsKey(true_label)){
				trueGuess.put(true_label, 0);
			}
			for(File test_f: test.get(label)){
				num_file_in_dir++;
				EachFile test_doc = new EachFile(false);	
				
				// build a map each word to count.
				test_doc.readFile(test_f);
				
				HashSet<String> test_voc = new HashSet<String>();

				// for each word, if the word is in the vocabulary set we used when training,
				// then add that word.
				for(String ori_word: test_doc.counting.keySet()){
					if(newTrain.vocabulary.contains(ori_word)){
						test_voc.add(ori_word);
					}
				}

				// Using the set of the word we added into the test_voc, predict the class.
				String test_opinion= calculateMaxNB((HashSet<String>) test_voc, newTrain);
				if(predict.containsKey(test_opinion)){
					Integer cnt = predict.get(test_opinion);
					cnt++;
					predict.put(test_opinion, cnt);
				} else {
					predict.put(test_opinion, 1);
				}
				
				//increment accuracy
				if(test_opinion.equals(true_label)){
					true_guess++;
				}
			}	
			
			// calculate recall
			output.println("recall for class " + label + ": " + true_guess/(double)num_file_in_dir);	
			trueGuess.put(true_label, true_guess);
		}
		
		// calculate precision
		for(String category: predict.keySet()){
			int bottom = predict.get(category).intValue();
			int top = trueGuess.get(category).intValue();
			
			if(bottom != 0){
				output.println("precision for class " + category + ": " + top/(double)bottom);
			} else {
				output.println("precision for class " + category + ": the program guessed there shouldn't be any food in this class");
			}
		}
	}

	/*
	 * Main function.
	 * We get four arguments
	 * first one is source directory for training.
	 * second one is test directory for testing.
	 * third one is output file.
	 * fourth one is using validator or not. I will set true to use it.
	 * If you don't want to use it, you can modify script file.
	 * 
	 * It calls Train class to train it, then test the trained program here with 
	 * the above methods.
	 */
	public static void main(String[] args) throws IOException {
		String source = args[0];
		String test = args[1];
		String output = args[2];
		String cross_valid = args[3];
		boolean cross_v;
		
		if(cross_valid.startsWith("t")){
			cross_v = true;
		} else {
			cross_v = false;
		}
		
		System.out.println("Training now...");
		Train newTrain1 = new Train();
		newTrain1.learnNaiveBayesText(source);
		System.out.println("Predicting now...");
		Test newTest1 = new Test(output);
		newTest1.classifyNaiveBayesText(test, output, newTrain1);
		System.out.println("done!!");
		
		PrintStream output_rp = new PrintStream(new File("testcross.ouput"));
		
		if(cross_v){
			for(int i = 1; i <= 10; i++){
				System.out.println("Training now...");
				Train newTrain = new Train(cross_v, i);
				newTrain.learnNaiveBayesText(source);
				System.out.println("Predicting now...");
				Test newTest = new Test();
				newTest.classifyNaiveBayesTextCross(newTrain.test_files, output_rp, newTrain, i);
				System.out.println("done!! " + i + "th validation.");
			}
		}
		System.out.println("done!! Cross_validation");
		
		
	}
}
