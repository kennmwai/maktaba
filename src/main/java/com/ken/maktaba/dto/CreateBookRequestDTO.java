package com.ken.maktaba.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBookRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author name cannot be longer than 255 characters")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 20, message = "ISBN cannot be longer than 20 characters")
    private String isbn;

    @NotNull(message = "Publication year is required")
    @Positive(message = "Publication year must be a positive number")
    private Integer publicationYear;

    @Size(max = 1000, message = "Description cannot be longer than 1000 characters")
    private String description;

    @Size(max = 2048, message = "Image URL cannot be longer than 2048 characters")
    @URL(message = "Image URL must be a valid URL") // TODO: Add URL validation
    private String imageUrl;
}