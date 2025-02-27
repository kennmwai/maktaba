package com.ken.maktaba.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ken.maktaba.dto.BookDTO;
import com.ken.maktaba.dto.CreateBookRequestDTO;
import com.ken.maktaba.dto.UpdateBookRequestDTO;
import com.ken.maktaba.entity.Insights;

public interface BookService {
	BookDTO createBook(CreateBookRequestDTO createBookRequestDTO);

	void deleteBook(Long id);

	Page<BookDTO> getAllBooks(Pageable pageable);

	BookDTO getBookById(Long id);

	List<BookDTO> searchBooks(String title, String author, String isbn, String query);

	BookDTO updateBook(Long id, UpdateBookRequestDTO updateBookRequestDTO);

	Insights getBookInsights(Long bookId);
}