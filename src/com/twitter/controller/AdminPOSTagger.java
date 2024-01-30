package com.twitter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.twitter.bean.RealTweetStream;
import com.twitter.model.RunLDA;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Servlet implementation class AdminPOSTagger
 */
@WebServlet("/AdminPOSTagger")
public class AdminPOSTagger extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminPOSTagger() {
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
		MaxentTagger tagger = new MaxentTagger("C:\\Users\\Tahesin\\Desktop\\newworkspace\\HateSpeech\\src\\english-left3words-distsim.tagger");
		HashMap<Integer, ArrayList<RealTweetStream>> clusterTwitList = (HashMap<Integer, ArrayList<RealTweetStream>>)session.getAttribute("ClusterTweetList");
		
		if(clusterTwitList!=null){
			for(Entry<Integer, ArrayList<RealTweetStream>> entry: clusterTwitList.entrySet()){
				int key = entry.getKey();
				ArrayList<RealTweetStream> comList = clusterTwitList.get(key);	
				ArrayList<String> revList=new ArrayList<String>();
				for(int i=0;i<comList.size();i++){
					HashSet<String> topics=new HashSet<String>();
					ArrayList<String> topicsl=new ArrayList<String>();

					revList.add(comList.get(i).getTweetContent());
					String tagged=tagger.tagString(comList.get(i).getTweetContent());
					comList.get(i).setPosTag(tagged);
					String tagStr[]=tagged.split(" ");
					for(int j=0;j<tagStr.length-1;j++){
						if(tagStr[j].contains("_JJ")&& !tagStr[j].contains("@")){
							String var[]=tagStr[j].split("_");	
							topics.add(var[0]);
						}if(tagStr[j].contains("_VBP")||tagStr[j].contains("_VBN")||tagStr[j].contains("_NN")){
							String var[]=tagStr[j].split("_");	
							topics.add(var[0]);
						}
					}
					topicsl.addAll(topics);
					comList.get(i).setTopics(topicsl);
				}
				RunLDA run = new RunLDA();
				
				ArrayList<ArrayList<String>> topicKeywords=run.comprehensiveLDATest1(comList.size(),revList);
				ArrayList<String> topkey=new ArrayList<String>();
		          ArrayList<Double> topProb=new ArrayList<Double>();
				for (int i = 0; i < comList.size(); i++) {
					for (int j = 0; j < topicKeywords.get(i).size(); j++) {
						String splitStr = topicKeywords.get(i).get(j);
						String sStr[] = splitStr.split(":");
						topkey.add(sStr[0]);
						topProb.add(Double.parseDouble(sStr[1]));
					}
					comList.get(i).setTopicKeywords(topkey);
					comList.get(i).setTopicKeyProb(topProb);
				}
				clusterTwitList.put(key, comList);
			}
		}
		session.setAttribute("ClusterTweetList", clusterTwitList);		
		RequestDispatcher rd = request.getRequestDispatcher("admin_keywords.jsp");
		rd.forward(request, response);
	}

}
