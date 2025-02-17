package com.ken.maktaba.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ken.maktaba.entity.Book;
import com.ken.maktaba.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
		Book savedBook = bookService.createBook(book);
		return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{id}/ai-insights")
	public ResponseEntity<String> getAiInsights(@PathVariable Long id) {
		String aiInsights = bookService.getAiInsights(id);
		return new ResponseEntity<>(aiInsights, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> books = bookService.getAllBooks();
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Long id) {
		Book book = bookService.getBookById(id);
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Book>> searchBooks(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String author) {
		List<Book> books = bookService.searchBooks(title, author);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
		Book updatedBook = bookService.updateBook(id, book);
		return new ResponseEntity<>(updatedBook, HttpStatus.OK);
	}

}
