package com.twitter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


import twitter4j.conf.ConfigurationBuilder;

import com.twitter.bean.RealTweetStream;
import com.twitter.dao.SelectTweet;
import com.twitter.services.AdminService;
import com.twitter.services.AdminServiceImpl;

/**
 * Servlet implementation class AdminRealTweetController
 */
@WebServlet("/AdminRealTweetController")
public class AdminRealTweetController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminRealTweetController() {
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
		/*ConfigurationBuilder cb = new ConfigurationBuilder();
		 cb.setDebugEnabled(true)
         .setOAuthConsumerKey("k7PQSk89oOckA4o9l1oiEYr8D")
         .setOAuthConsumerSecret("yOYXs8ZIIQYPIkhUAX5282J3NwEcHFdYek9FnkNJoX5UifKB5j")
         .setOAuthAccessToken("848033061597986816-d3uOUswVRCpRAfrJxHMKXJe2GVrV622")
         .setOAuthAccessTokenSecret("EJ5pgOLUHBtJ1lqS1McaIeaA2CB3GSNWax9MY6vF5bLeO");
      cb.setDebugEnabled(true)
        .setOAuthConsumerKey("3nYp3H6ndFxV4HuqgzqMB6b6Q")
        .setOAuthConsumerSecret("enOKK2FyAzcYnMU8wtUEKkftOuWtEKEAkddekNAHfOsxGG3zOW")
        .setOAuthAccessToken("857571256761290752-NHOFAU6Oq9jRVDcEHwWt3frU178HAe5")
        .setOAuthAccessTokenSecret("MjYbRGENC1s6hZAUUSYrzA3x0e1Hhz5NUWejNUeDTW6Ic");
		 .setOAuthConsumerKey("k7PQSk89oOckA4o9l1oiEYr8D")
         .setOAuthConsumerSecret("yOYXs8ZIIQYPIkhUAX5282J3NwEcHFdYek9FnkNJoX5UifKB5j")
         .setOAuthAccessToken("848033061597986816-d3uOUswVRCpRAfrJxHMKXJe2GVrV622")
         .setOAuthAccessTokenSecret("EJ5pgOLUHBtJ1lqS1McaIeaA2CB3GSNWax9MY6vF5bLeO")

        .setTweetModeExtended(true);;

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter4j.Twitter tw = tf.getInstance();
      //  Status st= tw.updateStatus("Welcome to twiiter world@@@@");
        System.out.println("Twitter updated");
        //System.out.println(st.getUser().getName()+":"+st.getText());
*/        
		SelectTweet select=new SelectTweet();
		ArrayList<RealTweetStream> realTweetList = select.selectTweet();
        
        
                
         if(realTweetList!=null && realTweetList.size()>0){
        	 

        		 HttpSession session = request.getSession();
        		 session.setAttribute("RealTweetStream", realTweetList);
        		 RequestDispatcher rd = request.getRequestDispatcher("admin_realtweetstream.jsp");
        		 rd.forward(request, response);
        	 }
         
	}

}
