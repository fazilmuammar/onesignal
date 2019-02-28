package com.example.ekreasi.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class ResponseSearch{

	@SerializedName("result")
	private List<ResultSearch> result;

	public void setResult(List<ResultSearch> result){
		this.result = result;
	}

	public List<ResultSearch> getResult(){
		return result;
	}
}