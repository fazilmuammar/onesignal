package com.example.ekreasi.model;


import com.google.gson.annotations.SerializedName;


public class ResponseAddArticle{

	@SerializedName("response")
	private String response;

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}
}