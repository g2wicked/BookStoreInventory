package com.limonica.bookstoreinventory;

public class Book {
	private long ISBN;
	private String Title;
	
	public void setISBN(long newISBN) {
		this.ISBN = newISBN;
	}
	
	public long getISBN () {
		return ISBN;
	}
	
	public void setTitle(String newTitle) {
		this.Title = newTitle;
	}
	
	public String getTitle() {
		return Title;
	}
	
	@Override
	public String toString() {
		return Title + "(" + ISBN + ")";
	}

}
