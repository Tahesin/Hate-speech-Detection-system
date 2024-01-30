package com.twitter.dao;

import java.util.ArrayList;

import com.twitter.bean.Admin;
import com.twitter.bean.RealTweetStream;
import com.twitter.bean.Tweet;


public interface AdminDao {
	Admin selectAdmin(String email, String password);

	boolean updateAdmin(String oldPassword, String newPassword, String email);

	ArrayList<Tweet> selectTweet();

	boolean addRealStreamDataset(ArrayList<RealTweetStream> realTweetList);
}
