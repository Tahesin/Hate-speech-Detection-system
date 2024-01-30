package com.twitter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.twitter.bean.RealTweetStream;
import com.twitter.model.Porter;
import com.twitter.model.SentiWordNetDemoCode;

/**
 * Servlet implementation class AdminSentimentController
 */
@WebServlet("/AdminSentimentController")
public class AdminSentimentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminSentimentController() {
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
		long startNlp = System.currentTimeMillis();
		ArrayList<RealTweetStream> twitList=(ArrayList<RealTweetStream>)session.getAttribute("RealTweetStream");
		String pathToSWN = "C:/Users/Tahesin/Desktop/newworkspace/HateSpeech/WebContent/upload/SentiWordNet_3.0.0_20130122.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
		double pos=0.0;
		double neg=0.0;
		double neutral=0.0;
		double posScore=0.0;
		double negScore=0.0;
		int pc=0,nc=0;
		String words="";
		String word[]=null;
		String word1[]=null;
		String sentence="";		
		
		if(twitList!=null&&twitList.size()>0){				
			for(int i=0;i<twitList.size();i++){
				RealTweetStream real=twitList.get(i);
				pos=0.0;
				neg=0.0;
				neutral=0.0;
				pc=0;
				nc=0;
				posScore=0.0;
				negScore=0.0;
				sentence="";
				words=real.getTweetContent();
				//System.out.println(words);
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
					//System.out.println("word["+j+"]="+word[j]);
					if(word[j].length()>0)
					word[j]=porter.stripSuffixes(word[j]);
					sentence = sentence + word[j]+" ";
				}
				real.setStemming(sentence);
				
				String senti = sentence;
				word1=senti.split(" ");
				for(String ss:word1){
					try{
						pos = pos + (Double)sentiwordnet.posextract(ss);
						pc++;
					}catch(Exception e){
						e.printStackTrace();
					}
					try{
						neg = neg + (Double)sentiwordnet.negextract(ss);
						nc++;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				posScore=pos;
				negScore=neg;
				double score = posScore-negScore;
				String result="";
				if(score==0){
					result="Neutral";
					real.setPosScore(posScore);
					real.setNegScore(negScore);
					real.setSentiResult(result);
					
				}else if(score<0){
					result="Negative";
					real.setPosScore(posScore);
					real.setNegScore(negScore);
					real.setSentiResult(result);
					
				}else{
					result="Positive";
					real.setPosScore(posScore);
					real.setNegScore(negScore);
					real.setSentiResult(result);
					
				}
				
				twitList.set(i, real);
			}
			session.setAttribute("RealTweetStream", twitList);
			session.setAttribute("StartNLPTime", startNlp);
			RequestDispatcher rd = request.getRequestDispatcher("admin_senti.jsp");
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


}
