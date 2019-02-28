package com.example.ekreasi.model;


import com.google.gson.annotations.SerializedName;


public class ResultSearch {

	@SerializedName("love")
	private String love;

	@SerializedName("image")
	private String image;

	@SerializedName("content_id")
	private String contentId;

	@SerializedName("author")
	private String author;

	@SerializedName("created")
	private String created;

	@SerializedName("title")
	private String title;

	@SerializedName("view")
	private String view;

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("phone")
	private String phone;

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("category")
	private String category;

	@SerializedName("updated")
	private String updated;

	@SerializedName("status")
	private String status;

	public void setLove(String love){
		this.love = love;
	}

	public String getLove(){
		return love;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setContentId(String contentId){
		this.contentId = contentId;
	}

	public String getContentId(){
		return contentId;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setView(String view){
		this.view = view;
	}

	public String getView(){
		return view;
	}

	public void setCategoryId(String categoryId){
		this.categoryId = categoryId;
	}

	public String getCategoryId(){
		return categoryId;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
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

	public void setUpdated(String updated){
		this.updated = updated;
	}

	public String getUpdated(){
		return updated;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}