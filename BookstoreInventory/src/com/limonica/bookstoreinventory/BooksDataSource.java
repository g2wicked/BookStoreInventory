package com.limonica.bookstoreinventory;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BooksDataSource {
	
	// Database field
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NEWBOOKS };
	
	public BooksDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Book createBook(String book) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NEWBOOKS, book);
		long insertISBN = database.insert(MySQLiteHelper.TABLE_NEWBOOKS, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NEWBOOKS, allColumns, MySQLiteHelper.COLUMN_ID + " = " insertISBN, null, null, null, null);
		cursor.moveToFirst();
		Book newBook = cursorToBook(cursor);
		cursor.close();
		return newBook;
	}
	
	public void deleteBook(Book book) {
		long ISBN = book.getISBN();
		System.out.println("Book deleted with ISBN: " + ISBN);
		database.delete(MySQLiteHelper.TABLE_NEWBOOKS, MySQLiteHelper.COLUMN_ID + " = " + ISBN, null);
	}
	
	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<Book>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NEWBOOKS, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Book book = cursorToBook(cursor);
			books.add(book);
			cursor.moveToNext();
		}
		
		cursor.close();
		return books;
	}
	
	private Book cursorToBook(Cursor cursor) {
		Book book = new Book();
		book.setISBN(cursor.getLong(0));
		book.setTitle(cursor.getString(1));
		return book;
	}

}
