package com.ken.maktaba.service;

import java.util.List;

import com.ken.maktaba.entity.Book;
import com.ken.maktaba.entity.Insights;

public interface BookService {
	Book createBook(Book book);

	void deleteBook(Long id);

	List<Book> getAllBooks();

	Book getBookById(Long id);

	List<Book> searchBooks(String title, String author, String isbn, String query);

	Book updateBook(Long id, Book book);

	Insights getBookInsights(Long bookId);
}
