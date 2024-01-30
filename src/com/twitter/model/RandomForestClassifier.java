package com.twitter.model;

import weka.core.*;
import weka.classifiers.meta.FilteredClassifier;

import java.util.List;
import java.util.ArrayList;
import java.io.*;

import com.twitter.model.RandomForestClassifier;

/**
 * This class implements a simple text classifier in Java using WEKA.
 * It loads a file with the text to classify, and the model that has been
 * learnt with MyFilteredLearner.java.
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see MyFilteredLearner
 */
 public class RandomForestClassifier {

	/**
	 * String that stores the text to classify
	 */
	String text;
	/**
	 * Object that stores the instance.
	 */
	Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	FilteredClassifier classifier;
	public static ArrayList<String> textList = new ArrayList<String>();
	public static ArrayList<String> classifyTweet=new ArrayList<String>();
	public static ArrayList<Double> predProb=new ArrayList<Double>();
	/**
	 * This method loads the text to be classified.
	 * @param fileName The name of the file that stores the text.
	 */
	public void load(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			text = "";
			String str1="";
			while ((line = reader.readLine()) != null) {
				str1=str1+line;
				System.out.println(line);
				if(line.startsWith("\t")){				
					textList.add(str1.trim());
					str1="";
				}
				
            }
			System.out.println("===== Loaded text data: " + fileName + " =====");
			reader.close();
			System.out.println(text);
		}
		catch (IOException e) {
			System.out.println("Problem found when reading: " + fileName);
		}
	}
			
	/**
	 * This method loads the model to be used as classifier.
	 * @param fileName The name of the file that stores the text.
	 */
	public void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
            in.close();
 			System.out.println("===== Loaded model: " + fileName + " =====");
       } 
		catch (Exception e) {
			// Given the cast, a ClassNotFoundException must be caught along with the IOException
			System.out.println("Problem found when reading: " + fileName);
		}
	}
	
	/**
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public void makeInstance() {
		// Create the attributes, class and text
		FastVector fvNominalVal = new FastVector(12);
		fvNominalVal.addElement("Abusive");
		fvNominalVal.addElement("Comparison");
		fvNominalVal.addElement("Passingjudgement");
		fvNominalVal.addElement("Religious");
		fvNominalVal.addElement("Sarcasm");
		fvNominalVal.addElement("Whataboutery");
		fvNominalVal.addElement("Vulgar");
		fvNominalVal.addElement("Spam");
		fvNominalVal.addElement("Nonspam");
		
		Attribute attribute1 = new Attribute("class", fvNominalVal);
		Attribute attribute2 = new Attribute("tweetcontent",(FastVector) null);
		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		instances = new Instances("TestRelation", fvWekaAttributes, 1);           
		// Set class index
		instances.setClassIndex(0);
		// Create and add the instance
		for(int i=0;i<textList.size();i++){
		Instance instance = new Instance(2);
		instance.setValue(attribute2, textList.get(i));
		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		instances.add(instance);
		}
 		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
	}
	
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 */
	public void classify() {
		for(int i=0;i<instances.numInstances();i++){
			try {
				System.out.println(instances.instance(i));
				double pred = classifier.classifyInstance(instances.instance(i));
				System.out.println("===== Classified instance ====="+i);
				System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
				classifyTweet.add(instances.classAttribute().value((int) pred));
				predProb.add(pred);
			}
			catch (Exception e) {
				System.out.println("Problem found when classifying the text");
			}	
		}	
	}
	
	
} 