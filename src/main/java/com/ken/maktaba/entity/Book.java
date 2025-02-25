package com.ken.maktaba.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Title is required")
	@Size(max = 255, message = "Title cannot be longer than 255 characters")
	private String title;

	@NotBlank(message = "Author is required")
	@Size(max = 255, message = "Author name cannot be longer than 255 characters")
	private String author;

	@NotBlank(message = "ISBN is required")
	@Size(max = 20, message = "ISBN cannot be longer than 20 characters")
	private String isbn;

	@NotNull(message = "Publication year is required")
	@Positive(message = "Publication year must be a positive number")
	private Integer publicationYear;

	@Lob
	@Size(max = 1000, message = "Description cannot be longer than 1000 characters")
	private String description;

}