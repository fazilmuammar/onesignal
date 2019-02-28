package com.example.ekreasi.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ResponseArticleDetail{

	@SerializedName("result1")
	private List<ResultArticleDetail> result1;

	public void setResult1(List<ResultArticleDetail> result1){
		this.result1 = result1;
	}

	public List<ResultArticleDetail> getResult1(){
		return result1;
	}
}