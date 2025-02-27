package com.ken.maktaba.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.ken.maktaba.dto.BookDTO;
import com.ken.maktaba.dto.CreateBookRequestDTO;
import com.ken.maktaba.dto.UpdateBookRequestDTO;
import com.ken.maktaba.entity.Insights;
import com.ken.maktaba.mapper.BookMapper;
import com.ken.maktaba.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {
	private final BookService bookService;
	private final BookMapper bookMapper;

	public BookController(BookService bookService, BookMapper bookMapper) {
		this.bookService = bookService;
		this.bookMapper = bookMapper;
	}

	@PostMapping
	public ResponseEntity<BookDTO> createBook(@Valid @RequestBody CreateBookRequestDTO createBookRequestDTO) {
		BookDTO savedBookDTO = bookService.createBook(createBookRequestDTO);
		return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
		BookDTO bookDTO = bookService.getBookById(id);
		return new ResponseEntity<>(bookDTO, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,
			@Valid @RequestBody UpdateBookRequestDTO updateBookRequestDTO) {
		BookDTO updatedBookDTO = bookService.updateBook(id, updateBookRequestDTO);
		return new ResponseEntity<>(updatedBookDTO, HttpStatus.OK);
	}

	@GetMapping("/{id}/ai-insights")
	public ResponseEntity<Insights> getAiInsights(@PathVariable Long id) {
		Insights aiInsights = bookService.getBookInsights(id);
		return new ResponseEntity<>(aiInsights, HttpStatus.OK);
	}

	@GetMapping // Updated to handle pagination and return DTOs
	public ResponseEntity<Page<BookDTO>> getAllBooks(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<BookDTO> booksPage = bookService.getAllBooks(pageable);
		return new ResponseEntity<>(booksPage, HttpStatus.OK);
	}

	@GetMapping("/search") // Updated to return DTOs
	public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "isbn", required = false) String isbn,
			@RequestParam(value = "q", required = false) String query) {

		List<BookDTO> booksDTO = bookService.searchBooks(title, author, isbn, query);
		return new ResponseEntity<>(booksDTO, HttpStatus.OK);
	}
}