package com.example.ekreasi.model;


import com.google.gson.annotations.SerializedName;


public class ResultArticleDetail {

	@SerializedName("image")
	private String image;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("content_id")
	private String contentId;

	@SerializedName("phone")
	private String phone;

	@SerializedName("author")
	private String author;

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("category")
	private String category;

	@SerializedName("title")
	private String title;

	@SerializedName("content")
	private String content;

	@SerializedName("signature")
	private String signature;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setSignature(String signature){
		this.signature = signature;
	}
	public String getSignature(){
		return signature;
	}

	public void setCategoryId(String categoryId){
		this.categoryId = categoryId;
	}

	public String getCategoryId(){
		return categoryId;
	}

	public void setContentId(String contentId){
		this.contentId = contentId;
	}

	public String getContentId(){
		return contentId;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}
}