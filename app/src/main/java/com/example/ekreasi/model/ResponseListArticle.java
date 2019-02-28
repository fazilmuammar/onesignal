package com.example.ekreasi.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ResponseListArticle{

	@SerializedName("result")
	private List<ResultItemListArticle> result;

	public ResponseListArticle() {}

	public void setResult(List<ResultItemListArticle> result){
		this.result = result;
	}

	public List<ResultItemListArticle> getResult(){
		return result;
	}
}