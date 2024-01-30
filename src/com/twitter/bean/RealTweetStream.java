package com.twitter.bean;

import java.util.ArrayList;

public class RealTweetStream {
	private int twitId;
	private String userName;
	private String tweetContent;
	private long createdAcct;
	private int followers;
	private int followings;
	private int userfavourites;
	private int listed;
	private int tweetCount;
	private int retweetCount;
	private int hashtagCount;
	private int userMention;
	private int urlCount;
	private int charCount;
	private int digitCount;
	private double posScore;
	private double negScore;
	private String sentiResult;
	private String stemming;
	private double semanticScore;
	private String result;
	private double resultProb;
	private String posTag;
	private ArrayList<String> topics;
	private ArrayList<String> topicKeywords;
	private ArrayList<Double> topicKeyProb;
	private String class1;
	public int getTwitId() {
		return twitId;
	}
	public void setTwitId(int twitId) {
		this.twitId = twitId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTweetContent() {
		return tweetContent;
	}
	public void setTweetContent(String tweetContent) {
		this.tweetContent = tweetContent;
	}
	
	public long getCreatedAcct() {
		return createdAcct;
	}
	public void setCreatedAcct(long createdAcct) {
		this.createdAcct = createdAcct;
	}
	public int getFollowers() {
		return followers;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	public int getFollowings() {
		return followings;
	}
	public void setFollowings(int followings) {
		this.followings = followings;
	}
	public int getUserfavourites() {
		return userfavourites;
	}
	public void setUserfavourites(int userfavourites) {
		this.userfavourites = userfavourites;
	}
	public int getListed() {
		return listed;
	}
	public void setListed(int listed) {
		this.listed = listed;
	}
	public int getTweetCount() {
		return tweetCount;
	}
	public void setTweetCount(int tweetCount) {
		this.tweetCount = tweetCount;
	}
	public int getRetweetCount() {
		return retweetCount;
	}
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}
	public int getHashtagCount() {
		return hashtagCount;
	}
	public void setHashtagCount(int hashtagCount) {
		this.hashtagCount = hashtagCount;
	}
	public int getUserMention() {
		return userMention;
	}
	public void setUserMention(int userMention) {
		this.userMention = userMention;
	}
	public int getUrlCount() {
		return urlCount;
	}
	public void setUrlCount(int urlCount) {
		this.urlCount = urlCount;
	}
	public int getCharCount() {
		return charCount;
	}
	public void setCharCount(int charCount) {
		this.charCount = charCount;
	}
	public int getDigitCount() {
		return digitCount;
	}
	public void setDigitCount(int digitCount) {
		this.digitCount = digitCount;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}	
	
	public double getPosScore() {
		return posScore;
	}
	public void setPosScore(double posScore) {
		this.posScore = posScore;
	}
	public double getNegScore() {
		return negScore;
	}
	public void setNegScore(double negScore) {
		this.negScore = negScore;
	}
	public String getSentiResult() {
		return sentiResult;
	}
	public void setSentiResult(String sentiResult) {
		this.sentiResult = sentiResult;
	}
	public double getResultProb() {
		return resultProb;
	}
	public void setResultProb(double resultProb) {
		this.resultProb = resultProb;
	}
	public String getStemming() {
		return stemming;
	}
	public void setStemming(String stemming) {
		this.stemming = stemming;
	}
	public double getSemanticScore() {
		return semanticScore;
	}
	public void setSemanticScore(double semanticScore) {
		this.semanticScore = semanticScore;
	}
	public String getPosTag() {
		return posTag;
	}
	public void setPosTag(String posTag) {
		this.posTag = posTag;
	}
	public ArrayList<String> getTopics() {
		return topics;
	}
	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}
	public ArrayList<String> getTopicKeywords() {
		return topicKeywords;
	}
	public void setTopicKeywords(ArrayList<String> topicKeywords) {
		this.topicKeywords = topicKeywords;
	}
	public ArrayList<Double> getTopicKeyProb() {
		return topicKeyProb;
	}
	public void setTopicKeyProb(ArrayList<Double> topicKeyProb) {
		this.topicKeyProb = topicKeyProb;
	}
	public String getClass1() {
		return class1;
	}
	public void setClass1(String class1) {
		this.class1 = class1;
	}
	
	
}
