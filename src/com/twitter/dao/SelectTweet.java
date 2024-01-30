package com.twitter.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import com.twitter.bean.RealTweetStream;
import com.twitter.bean.Tweet;
import com.twitter.connection.ConnectionUtils;

public class SelectTweet {
	
	public ArrayList<RealTweetStream> selectTweet() {
		// TODO Auto-generated method stub
		ArrayList<RealTweetStream> realTweetList = new ArrayList<RealTweetStream>();
		
		String sql="Select * from tblRealTwitStream limit 30";

			
		try {
			PreparedStatement pstmt = ConnectionUtils.getConnection().prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				RealTweetStream review =new RealTweetStream();
				review.setTwitId(rs.getInt(1));
				review.setUserName(rs.getString(2));
				review.setTweetContent(rs.getString(3));
				review.setCreatedAcct(rs.getLong(4));
				review.setFollowers(rs.getInt(5));
				review.setFollowings(rs.getInt(6));
				review.setUserfavourites(rs.getInt(7));
				review.setListed(rs.getInt(8));
				review.setTweetCount(rs.getInt(9));
				review.setRetweetCount(rs.getInt(10));
				review.setHashtagCount(rs.getInt(11));
				review.setUserMention(rs.getInt(12));
				review.setUrlCount(rs.getInt(13));
				review.setCharCount(rs.getInt(14));
				review.setDigitCount(rs.getInt(15));
				
				
				realTweetList.add(review);
			}
			}catch(SQLException e)
			{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			return realTweetList;
	}

}
