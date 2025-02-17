package com.ken.maktaba.service;

import java.util.List;

import com.ken.maktaba.entity.Book;

public interface BookService {
    Book createBook(Book book);
    void deleteBook(Long id);
    String getAiInsights(Long id);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    List<Book> searchBooks(String title, String author);
    Book updateBook(Long id, Book book);
}
