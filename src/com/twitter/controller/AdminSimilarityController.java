package com.twitter.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.pfv.spmf.algorithms.clustering.text_clusterer.TextClusterAlgo;
import ca.pfv.spmf.test.MainTestTextClusterer;

import com.twitter.bean.Cluster;
import com.twitter.bean.RealTweetStream;

import com.twitter.model.Porter;
import com.twitter.model.lsa.SimilarityException;
import com.twitter.model.lsa.TextSimilarityMeasure;
import com.twitter.model.lsa.WordNGramJaccardMeasure;

/**
 * Servlet implementation class AdminSimilarityController
 */
@WebServlet("/AdminSimilarityController")
public class AdminSimilarityController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminSimilarityController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ArrayList<RealTweetStream> twitList=(ArrayList<RealTweetStream>)session.getAttribute("RealTweetStream");
		String file="C:/Users/Tahesin/Desktop/newworkspace/HateSpeech/WebContent/upload/input_text_clustering.txt";
		File file1 = new File(file);
		createFile(file, twitList);
		String words="";
		String word[]=null;		
		String sentence="";
		if(twitList!=null && twitList.size()>0){			
			for(int i=0;i<twitList.size();i++){
				sentence="";
				words=twitList.get(i).getTweetContent();
				words=words.toLowerCase();
				words=words.replace(".","");
				words=words.replace(",","");
				words=words.replace("!","");
				//words=words.replace(":d","");
				words=words.replace(":)","");
				words=words.replace(":","");
				words=words.replace(";","");
				words=words.replace("?","");
				words=words.replace("_","");
				words=words.replace("*","");
				words=words.replace("^","");
				words=words.replace("<3","");
				word=words.split("\\s+");
				for(int j=0;j<word.length;j++){
					word[j]=removeDup(word[j]);
				}
				for(int j=0;j<word.length;j++){
					Porter porter = new Porter();
					if(!word[j].equals("")){
					word[j]=porter.stripSuffixes(word[j]);
					}
					sentence = sentence + word[j]+" ";
				}
				twitList.get(i).setStemming(sentence);
			}
		}
		if(twitList!=null&&twitList.size()>0){
			for(int i=0;i<twitList.size()-1;i++){
				String content1=twitList.get(i).getStemming();
				String[] tokens1 = content1.split(" ");
				double score=0;
				for(int j=0;j<twitList.size();j++){
					if(i!=j){				
					String content2=twitList.get(j).getStemming();
					TextSimilarityMeasure measure = new WordNGramJaccardMeasure(3);    // Use word trigrams
					
					String[] tokens2 = content2.split(" ");					
					try {
						double sscore=measure.getSimilarity(tokens1, tokens2);
						
							score = score+sscore;
							System.out.println("Similarity: " + score);
						
					} catch (SimilarityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
				twitList.get(i).setSemanticScore(score);
			}
		}
		
		HashMap<Integer, ArrayList<RealTweetStream>> clusterTweetList = new  HashMap<Integer, ArrayList<RealTweetStream>>();
		ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
		Set<Integer> clusterSet=new HashSet<Integer>();
		String outFile="C:/Users/Tahesin/Desktop/newworkspace/HateSpeech/WebContent/upload/output.txt";
		if (twitList != null) {
			TextClusterAlgo textClusterer = new TextClusterAlgo();
			textClusterer.runAlgorithm("C:/Users/Tahesin/Desktop/newworkspace/HateSpeech/WebContent/upload/input_text_clustering.txt", "C:/Users/Tahesin/Desktop/newworkspace/HateSpeech/WebContent/upload/output.txt",true,true);
			
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(outFile));
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				if(!sCurrentLine.contains("RecordId")){
					Cluster cluster=new Cluster();
					String str[]=sCurrentLine.split("\t");
					int commentid=Integer.parseInt(str[0]);
					RealTweetStream onComment = new RealTweetStream();
					for(int i=0;i<twitList.size();i++){
						if(commentid==twitList.get(i).getTwitId()){							
							onComment=twitList.get(i);
							break;
						}
					}
					
					int clusterId=Integer.parseInt(str[1]);
					clusterSet.add(clusterId);
					cluster.setClusterid(clusterId);
					cluster.setOnComment(onComment);
					clusterList.add(cluster);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if(clusterList!=null&&clusterList.size()>0){
			for(int i=0;i<clusterList.size()-1;i++){
				int clid=clusterList.get(i).getClusterid();
				ArrayList<RealTweetStream> comList= new ArrayList<RealTweetStream>();
				comList.add(clusterList.get(i).getOnComment());
				for(int j=i+1;j<clusterList.size();j++){
					if(clid==clusterList.get(j).getClusterid()){
						comList.add(clusterList.get(j).getOnComment());
					}else{
						i=j-1;
						clusterTweetList.put(clid, comList);
						break;
					}
				}
				
			}
			session.setAttribute("ClusterTweetList", clusterTweetList);
			session.setAttribute("ClusterSet", clusterSet);
			RequestDispatcher rd = request.getRequestDispatcher("similarity_cluster.jsp");
			rd.forward(request, response);
		}

	}
	public  String removeDup(String str){
		str = str + " "; // Adding a space at the end of the word
        int l=str.length(); // Finding the length of the word
		String ans="";
		char ch1,ch2;
				 
        for(int i=0; i<l-1; i++)
        {
            ch1=str.charAt(i); // Extracting the first character
            ch2=str.charAt(i+1); // Extracting the next character
 
// Adding the first extracted character to the result if the current and the next characters are different
            int count= countRun(str, ch1);
            if(count==2){
            	ans = ans + ch1;
            }else if(ch1!=ch2){            
            	ans = ans + ch1;
            }            
        }
	    return ans;
    }
	public static int countRun( String s, char c )
	{
	    int counter = 0;
	    for( int i = 0; i < s.length(); i++)
	    {
	      if( s.charAt(i) == c )  counter++;
	      else if(counter>0) break;
	    }
	    return counter;
	}
	private void createFile(String file, ArrayList<RealTweetStream> commentList)
            throws IOException {
        FileWriter writer = new FileWriter(file);
        int size = commentList.size();
        for (int i=0;i<size;i++) {
        	
            String str = commentList.get(i).getTwitId()+"\t"+commentList.get(i).getStemming();
            writer.write(str);
            if(i < size-1)//This prevent creating a blank like at the end of the file**
                writer.write("\n");
        }
        writer.close();
    }

}
