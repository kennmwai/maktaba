package com.ken.maktaba.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ken.maktaba.entity.Book;
import com.ken.maktaba.exception.ResourceNotFoundException;
import com.ken.maktaba.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Override
    public String getAiInsights(Long id) {
        // AI Insights Logic will be implemented later
        // For now, return a placeholder
        return "AI insights are not yet implemented.";
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public List<Book> searchBooks(String title, String author) {
        if (title != null && author != null) {
            return bookRepository.searchBooksByTitleAndAuthor(title, author);
        } else if (title != null) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        } else if (author != null) {
            return bookRepository.findByAuthorContainingIgnoreCase(author);
        } else {
            return getAllBooks(); // Or return empty list if no search criteria provided based on requirement.
        }
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setPublicationYear(book.getPublicationYear());
        existingBook.setDescription(book.getDescription());
        return bookRepository.save(existingBook);
    }
}
