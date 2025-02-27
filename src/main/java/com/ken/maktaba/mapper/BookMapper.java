package com.ken.maktaba.mapper;

import org.springframework.stereotype.Component;

import com.ken.maktaba.dto.BookDTO;
import com.ken.maktaba.dto.CreateBookRequestDTO;
import com.ken.maktaba.dto.UpdateBookRequestDTO;
import com.ken.maktaba.entity.Book;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setDescription(book.getDescription());
        dto.setImageUrl(book.getImageUrl());
        return dto;
    }

    public Book toEntity(CreateBookRequestDTO createBookRequestDTO) {
        if (createBookRequestDTO == null) {
            return null;
        }
        Book book = new Book();
        book.setTitle(createBookRequestDTO.getTitle());
        book.setAuthor(createBookRequestDTO.getAuthor());
        book.setIsbn(createBookRequestDTO.getIsbn());
        book.setPublicationYear(createBookRequestDTO.getPublicationYear());
        book.setDescription(createBookRequestDTO.getDescription());
        book.setImageUrl(createBookRequestDTO.getImageUrl());
        return book;
    }

    public Book toEntity(UpdateBookRequestDTO updateBookRequestDTO, Book existingBook) {
        if (updateBookRequestDTO == null || existingBook == null) {
            return existingBook; // or throw exception, depending on desired behavior
        }
        existingBook.setTitle(updateBookRequestDTO.getTitle());
        existingBook.setAuthor(updateBookRequestDTO.getAuthor());
        existingBook.setIsbn(updateBookRequestDTO.getIsbn());
        existingBook.setPublicationYear(updateBookRequestDTO.getPublicationYear());
        existingBook.setDescription(updateBookRequestDTO.getDescription());
        existingBook.setImageUrl(updateBookRequestDTO.getImageUrl());
        return existingBook;
    }
}