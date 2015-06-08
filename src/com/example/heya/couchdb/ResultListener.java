package com.example.heya.couchdb;

public interface ResultListener<T> {
	public void onResultsSucceded(T result);
	public void onResultsFail();
}
