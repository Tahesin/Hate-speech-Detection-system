package com.twitter.model;

public interface Probability<T, K> {

    public float featureProbability(T feature, K category);

}