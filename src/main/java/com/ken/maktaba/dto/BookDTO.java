package com.ken.maktaba.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private String description;
    private String imageUrl;
}
