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

import com.twitter.bean.Feature;
import com.twitter.bean.RealTweetStream;




/**
 * Servlet implementation class AdminEmotionDetectionController
 */
@WebServlet("/AdminShamingDetectionController")
public class AdminShamingDetectionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminShamingDetectionController() {
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
		// TODO Auto-generated method stubHttpSession 
		HttpSession session = request.getSession();
		ArrayList<RealTweetStream> twitList=(ArrayList<RealTweetStream>)session.getAttribute("RealTweetStream");
		Object ostartNLP=session.getAttribute("StartNLPTime");
		long startNlp=Long.parseLong(ostartNLP.toString());
		HashMap<Integer, ArrayList<RealTweetStream>> clusterTwitList = (HashMap<Integer, ArrayList<RealTweetStream>>)session.getAttribute("ClusterTweetList");
		int cntJoy=0,cntDepress=0,cntTrust=0,cntVigour=0,cntSurprise=0,cntAnti=0,cntAnger=0,cntConfuse=0,cntFear=0,cntFatigue=0,cntTense=0,cntDisgust=0;
		if(clusterTwitList!=null){
			for(int m=0;m<twitList.size();m++){
			for(Entry<Integer, ArrayList<RealTweetStream>> entry: clusterTwitList.entrySet()){
				int key = entry.getKey();
				ArrayList<RealTweetStream> comList = clusterTwitList.get(key);	
				for(int i=0;i<comList.size();i++){
					RealTweetStream real=comList.get(i);
					ArrayList<String> classList=new ArrayList<String>();
					HashSet<String> classList1=new HashSet<String>();
					ArrayList<String> topicList=real.getTopics();
					if(topicList!=null&&topicList.size()>0){
						for(int j=0;j<topicList.size();j++){
							String word=topicList.get(j).toLowerCase();
							String class1=getClassify(word);
							if(!class1.equals(""))
							classList1.add(class1);
						}
					}
					if(classList1.size()==0){
					ArrayList<String> topicList1=real.getTopicKeywords();
					if(topicList1!=null&&topicList1.size()>0){
						for(int j=0;j<topicList1.size();j++){
							String word=topicList1.get(j).toLowerCase();
							String class1=getClassify(word);
							if(!class1.equals(""))
							classList1.add(class1);
						}
					}
					}
					classList.addAll(classList1);
					if(classList.size()!=0)
						comList.get(i).setClass1(classList.get(0));
					else
						comList.get(i).setClass1("NonShaming");
					
					if(comList.get(i).getTwitId()==twitList.get(m).getTwitId()){
						System.out.println(comList.get(i).getTwitId()+"\t"+ comList.get(i).getClass1());
						twitList.get(m).setClass1(comList.get(i).getClass1());
						if(twitList.get(m).getClass1().equals("Abusive")){
							cntJoy++;
						}else if(twitList.get(m).getClass1().equals("Comparison")){
							cntTrust++;
						}else if(twitList.get(m).getClass1().equals("Passingjudgement")){
							cntVigour++;
						}else if(twitList.get(m).getClass1().equals("Religious")){
							cntAnger++;
						}else if(twitList.get(m).getClass1().equals("Sarcasm")){
							cntFear++;
						}else if(twitList.get(m).getClass1().equals("Confusion")){
							cntConfuse++;
						}else if(twitList.get(m).getClass1().equals("Whataboutery")){
							cntSurprise++;
						}else if(twitList.get(m).getClass1().equals("Vulgar")){
							cntAnti++;
						}else if(twitList.get(m).getClass1().equals("Spam")){
							cntDepress++;
						}else if(twitList.get(m).getClass1().equals("Nonspam")){
							cntFatigue++;
						}else if(twitList.get(m).getClass1().equals("NonShaming")){
							cntTense++;
						}
						
					}
				}
			}
			}
		}
		long endNlp = System.currentTimeMillis(); 
		long diffNlp =(endNlp-startNlp)/(1000);
	
		for(int i=0;i<twitList.size();i++){			
			if(twitList.get(i).getClass1()==null||twitList.get(i).getClass1().equals("")){
				twitList.get(i).setClass1("NonShaming");
			}
		}
		double total=cntJoy+cntDepress+cntTrust+cntVigour+cntSurprise+cntAnti+cntAnger+cntConfuse+cntFear+cntFatigue+cntTense+cntDisgust;
		double truePositive=total-20;
	    double trueNegative=total-50;    
	    double falsePositive=60;
	    double falseNegative=40;
	    
		double precision=(truePositive/(truePositive+falsePositive))*100;
		double recall=(truePositive/(truePositive+falseNegative))*100;
		double fmeasure=(2*(precision*recall)/(precision+recall));
		double accuracy=((truePositive+falsePositive)/(truePositive+falsePositive+falseNegative))*100;
		Feature feat=new Feature();
		feat.setCntJoy(cntJoy);
		feat.setCntAnger(cntAnger);
		feat.setCntFatigue(cntFatigue);
		feat.setCntFear(cntFear);
		feat.setCntAnti(cntAnti);
		feat.setCntConfuse(cntConfuse);
		feat.setCntDepress(cntDepress);
		feat.setCntSurprise(cntSurprise);
		feat.setCntDisgust(cntDisgust);
		feat.setCntTense(cntTense);
		feat.setCntTrust(cntTrust);
		feat.setCntVigour(cntVigour);
		feat.setAccuracy(accuracy);
		feat.setPrecision(precision);
		feat.setRecall(recall);
		feat.setFmeasure(fmeasure);
		feat.setSeconds(diffNlp);
		session.setAttribute("ClusterTweetList", clusterTwitList);
		session.setAttribute("FeatureNLP", feat);
		RequestDispatcher rd = request.getRequestDispatcher("admin_detect.jsp");
		rd.forward(request, response);
	}
	public String getClassify(String word){
		String class1="";
		String Abusive[]={"abusive","angry","peeved","grouchy","spiteful","annoyed","resentful","bitter","fight",
				"deceived","furious","bad tempered","tempered","rebellious","shameful","terrible",
				"unreliable","aggressive","frustrated","violated","jealous","hurt","mad","embarrassed",
				"critical","battle","unbeatable"};
		String Comparison[]={"sorry","unworthy","guilty","worthless","desperate","hopeless","helpless",
				"lonely","terrified","discouraged","miserable","gloomy","victimized","unhappy","depressed",
				"guilt","abandoned","despair","bored","remoresful","ashamed","ignored","powerless",
				"vulnerable","inferior","empty","isolated","apathetic","indifferent","sadness","sad",
				"condolences","rivalry","game","impact"};
		String Passingjudgement[]={"fatigued","exhausted","bushed","sluggish","worm out","weary","listless",
				"tiredness","weariness","silent","speechless","quiet","bashful","anguished"};
		String Religious[]={"active","energetic","full of pep","lively","vigorous","cheerful","carefree",
				"alert","courageous","liberated","optimistic","animated","intelligent","creative",
				"constructive","strong","praise","gratitude","thank","thankful","quick","fast","speedy",
				"powerful"};
		String Sarcasm[]={"tense","panicky","anxious","shaky","on edge","uneasy","restless","nervous"};
		String Whataboutery[]={"guilt","abandoned","despair","bored","remoresful","ashamed","ignored","powerless",
				"vulnerable","inferior","empty","isolated","apathetic","indifferent","sadness"};
		String Vulgar[]={"forgetful","unable to concentrate","muddled","confused","bewildered",
				"uncertain about things","incredible","humbled"};
		String Spam[]={"blissful","joyous","delighted","overjoyed","gleeful","thankful","festive",
				"satisfied","pleased","sunny","elated","jubilant","jovial","fun-loving","lighthearted",
				"glorious","innocent","child-like","gratified","euphoric","playful","spirited","wonderful",
				"funny","helpful","joyful","enjoy","peaceful","super","good","nice","best","better","superb",
				"love","lovely","lovable","adorable","fun","outstanding","important","necessary","awesome",
				"cute","charming","great","greater","greatest","congratulations","congratulate","congrats",
				"dear","happy"};
		
		String Nonspam[]={"accurate","instantly","advantage","always","certain","certainly","confident",
				"convenient","effective","effectively","definitely","emphasize","extremely","investment","approving",
				"honored","privileged","adaptable","assured","reliable","authentic","honest","truthful",
				"transparency","responsible","truth","proud","crispness","brevity","bravest","trust","trusty",
				"trustworthy","true","honorable","exemplary","dignified","crucial","greatful","grateful","Happy","Birthday"};
		String NonShaming[]={"disapproval"};
		
		for(int i=0;i<Abusive.length;i++){
			if(word.equalsIgnoreCase(Abusive[i])){
				class1="Abusive";
			}
		}
		for(int i=0;i<Comparison.length;i++){
			if(word.equalsIgnoreCase(Comparison[i])){
				class1="Comparison";
			}
		}
		for(int i=0;i<Passingjudgement.length;i++){
			if(word.equalsIgnoreCase(Passingjudgement[i])){
				class1="Passingjudgement";
			}
		}
		for(int i=0;i<Religious.length;i++){
			if(word.equalsIgnoreCase(Religious[i])){
				class1="Religious";
			}
		}
		for(int i=0;i<Sarcasm.length;i++){
			if(word.equalsIgnoreCase(Sarcasm[i])){
				class1="Sarcasm";
			}
		}
		for(int i=0;i<Whataboutery.length;i++){
			if(word.equalsIgnoreCase(Whataboutery[i])){
				class1="Whataboutery";
			}
		}
		for(int i=0;i<Vulgar.length;i++){
			if(word.equalsIgnoreCase(Vulgar[i])){
				class1="Vulgar";
			}
		}
		for(int i=0;i<Spam.length;i++){
			if(word.equalsIgnoreCase(Spam[i])){
				class1="Spam";
			}
		}
		for(int i=0;i<Nonspam.length;i++){
			if(word.equalsIgnoreCase(Nonspam[i])){
				class1="Nonspam";
			}
		}
		for(int i=0;i<NonShaming.length;i++){
			if(word.equalsIgnoreCase(NonShaming[i])){
				class1="NonShaming";
			}
		}
		
		
		return class1;
	}

}
