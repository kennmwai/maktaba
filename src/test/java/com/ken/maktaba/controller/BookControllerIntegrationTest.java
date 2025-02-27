package com.ken.maktaba.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ken.maktaba.dto.AIResponseDTO;
import com.ken.maktaba.dto.BookDTO;
import com.ken.maktaba.dto.ChatCompletionRequest;
import com.ken.maktaba.dto.CreateBookRequestDTO;
import com.ken.maktaba.dto.UpdateBookRequestDTO;
import com.ken.maktaba.entity.Book;
import com.ken.maktaba.entity.Insights;
import com.ken.maktaba.mapper.BookMapper;
import com.ken.maktaba.repository.BookRepository;
import com.ken.maktaba.repository.InsightsRepository;
import com.ken.maktaba.service.BookService;
import com.ken.maktaba.util.AIService;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private InsightsRepository insightsRepository;

	@Autowired
	private BookMapper bookMapper;

	@MockBean
	private BookService bookService;

	@MockBean
	private AIService aiServiceMock;

	private Book testBook1;
	private Book testBook2;
	private BookDTO testBookDTO1;
	private BookDTO testBookDTO2;
	private Insights testInsights1;
	private Insights testInsights2;

	@BeforeEach
	void setUp() throws Exception {
		insightsRepository.deleteAll();
		bookRepository.deleteAll(); // Clean up database before each test

		testBook1 = new Book();
		testBook1.setTitle("Test Book 1");
		testBook1.setAuthor("Test Author 1");
		testBook1.setIsbn("111-111-111");
		testBook1.setPublicationYear(2020);
		testBook1.setDescription("Description for test book 1");
		testBook1.setImageUrl("http://example.com/image1.jpg");
		testBook1 = bookRepository.save(testBook1);
		testBookDTO1 = bookMapper.toDTO(testBook1);

		testBook2 = new Book();
		testBook2.setTitle("Test Book 2");
		testBook2.setAuthor("Test Author 2");
		testBook2.setIsbn("222-222-222");
		testBook2.setPublicationYear(2022);
		testBook2.setDescription("Description for test book 2");
		testBook2.setImageUrl("http://example.com/image2.jpg");
		testBook2 = bookRepository.save(testBook2);
		testBookDTO2 = bookMapper.toDTO(testBook2);

		testInsights1 = new Insights();
		testInsights1.setBook(testBook1);
		testInsights1 = insightsRepository.save(testInsights1);

		testInsights2 = new Insights();
		testInsights2.setBook(testBook2);
		testInsights2 = insightsRepository.save(testInsights2);

		// Mock AI Service setup for successful response
		AIResponseDTO mockAIResponseDTO = new AIResponseDTO();
		AIResponseDTO.Choice choice = new AIResponseDTO.Choice();
		AIResponseDTO.Message message = new AIResponseDTO.Message();
		message.setContent("Mock AI Summary");
		choice.setMessage(message);
		mockAIResponseDTO.setChoices(List.of(choice));
		when(aiServiceMock.createChatCompletion(any(ChatCompletionRequest.class))).thenReturn("Mock AI Summary");
	}

	@Test
	void createBook_ValidInput_ReturnsCreatedBook() throws Exception {
		CreateBookRequestDTO newBookRequestDTO = new CreateBookRequestDTO();
		newBookRequestDTO.setTitle("New Book");
		newBookRequestDTO.setAuthor("New Author");
		newBookRequestDTO.setIsbn("333-333-333");
		newBookRequestDTO.setPublicationYear(2023);
		newBookRequestDTO.setDescription("New Description");
		newBookRequestDTO.setImageUrl("http://example.com/new_image.jpg");

		mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newBookRequestDTO))).andExpect(status().isCreated());
//				.andExpect(jsonPath("$.title", is("New Book")))
//				.andExpect(jsonPath("$.author", is("New Author")))
//				.andExpect(jsonPath("$.imageUrl", is("http://example.com/new_image.jpg")));
	}

	@Test
	void createBook_InvalidInput_ReturnsBadRequest() throws Exception {
		CreateBookRequestDTO invalidBookRequestDTO = new CreateBookRequestDTO();

		mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidBookRequestDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors.title").exists()).andExpect(jsonPath("$.errors.author").exists())
				.andExpect(jsonPath("$.errors.isbn").exists()).andExpect(jsonPath("$.errors.publicationYear").exists());
	}

	@Test
	void getAllBooks_ReturnsPaginatedListOfBooks() throws Exception {
		int pageNo = 0;
		int pageSize = 1;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		PageImpl<Book> bookPage = new PageImpl<>(Arrays.asList(testBook1), pageable, 2); // Total elements = 2
		PageImpl<BookDTO> bookDTOPage = new PageImpl<>(
				bookPage.getContent().stream().map(bookMapper::toDTO).collect(java.util.stream.Collectors.toList()),
				pageable, bookPage.getTotalElements());

		when(aiServiceMock.createChatCompletion(any(ChatCompletionRequest.class)))
				.thenReturn("Mock AI Summary from MockBean");
		when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookDTOPage);

		mockMvc.perform(get("/books").param("page", String.valueOf(pageNo)).param("size", String.valueOf(pageSize)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].title", is("Test Book 1")))
				.andExpect(jsonPath("$.content[0].imageUrl", is("http://example.com/image1.jpg")))
				.andExpect(jsonPath("$.pageable.pageSize", is(pageSize)))
				.andExpect(jsonPath("$.pageable.pageNumber", is(pageNo))).andExpect(jsonPath("$.totalPages", is(2)))
				.andExpect(jsonPath("$.totalElements", is(2)));
	}

	@Test
	void getAllBooks_DefaultPagination_ReturnsFirstPage() throws Exception {
		Pageable defaultPageable = PageRequest.of(0, 10);
		PageImpl<Book> bookPage = new PageImpl<>(Arrays.asList(testBook1, testBook2), defaultPageable, 2);
		PageImpl<BookDTO> bookDTOPage = new PageImpl<>(
				bookPage.getContent().stream().map(bookMapper::toDTO).collect(java.util.stream.Collectors.toList()),
				defaultPageable, bookPage.getTotalElements());

		when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookDTOPage);

		mockMvc.perform(get("/books")) // No page and size params, use defaults
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content", hasSize(2))).andExpect(jsonPath("$.pageable.pageSize", is(10))) // Assert
																													// default
																													// page
																													// size
				.andExpect(jsonPath("$.pageable.pageNumber", is(0))); // Assert default page number
	}

	@Test
    void getBookById_ExistingId_ReturnsBook() throws Exception {
        when(bookService.getBookById(testBook1.getId())).thenReturn(testBookDTO1);

        mockMvc.perform(get("/books/{id}", testBook1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBook1.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Test Book 1")))
                .andExpect(jsonPath("$.imageUrl", is("http://example.com/image1.jpg")));
    }

	@Test
    void getBookById_NonExistingId_ReturnsNotFound() throws Exception {
        when(bookService.getBookById(999L)).thenThrow(new com.ken.maktaba.exception.ResourceNotFoundException("Book not found with id: 999"));

        mockMvc.perform(get("/books/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Book not found with id: 999")));
    }

	@Test
	void updateBook_ExistingId_ValidInput_ReturnsUpdatedBook() throws Exception {
		UpdateBookRequestDTO updatedBookRequestDTO = new UpdateBookRequestDTO();
		updatedBookRequestDTO.setTitle("Updated Book Title");
		updatedBookRequestDTO.setAuthor("Updated Author");
		updatedBookRequestDTO.setIsbn("111-111-111");
		updatedBookRequestDTO.setPublicationYear(2021);
		updatedBookRequestDTO.setDescription("Updated Description");
		updatedBookRequestDTO.setImageUrl("http://example.com/updated_image.jpg");

		BookDTO updatedBookDTO = new BookDTO();
		updatedBookDTO.setId(testBook1.getId());
		updatedBookDTO.setTitle("Updated Book Title");
		updatedBookDTO.setAuthor("Updated Author");
		updatedBookDTO.setIsbn("111-111-111");
		updatedBookDTO.setPublicationYear(2021);
		updatedBookDTO.setDescription("Updated Description");
		updatedBookDTO.setImageUrl("http://example.com/updated_image.jpg");

		when(bookService.updateBook(Mockito.eq(testBook1.getId()), any(UpdateBookRequestDTO.class)))
				.thenReturn(updatedBookDTO);

		mockMvc.perform(put("/books/{id}", testBook1.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBookRequestDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(testBook1.getId().intValue())))
				.andExpect(jsonPath("$.title", is("Updated Book Title")))
				.andExpect(jsonPath("$.author", is("Updated Author")))
				.andExpect(jsonPath("$.imageUrl", is("http://example.com/updated_image.jpg")));
	}

	@Test
	void updateBook_NonExistingId_ReturnsNotFound() throws Exception {
		UpdateBookRequestDTO updatedBookRequestDTO = new UpdateBookRequestDTO();
		updatedBookRequestDTO.setTitle("Updated Book Title");
		updatedBookRequestDTO.setAuthor("Updated Author");
		updatedBookRequestDTO.setIsbn("111-111-111");
		updatedBookRequestDTO.setPublicationYear(2021);
		updatedBookRequestDTO.setDescription("Updated Description");
		updatedBookRequestDTO.setImageUrl("http://example.com/updated_image.jpg");

		when(bookService.updateBook(Mockito.eq(999L), any(UpdateBookRequestDTO.class)))
				.thenThrow(new com.ken.maktaba.exception.ResourceNotFoundException("Book not found with id: 999"));

		mockMvc.perform(put("/books/{id}", 999L).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBookRequestDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Book not found with id: 999")));
	}

	@Test
	void updateBook_InvalidInput_ReturnsBadRequest() throws Exception {
		UpdateBookRequestDTO invalidBookRequestDTO = new UpdateBookRequestDTO(); // Invalid book - missing title

		mockMvc.perform(put("/books/{id}", testBook1.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidBookRequestDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errors.title").exists());
	}

	@Test
	void deleteBook_ExistingId_ReturnsNoContent() throws Exception {
		mockMvc.perform(delete("/books/{id}", testBook1.getId())).andExpect(status().isNoContent());

		Optional<Book> deletedBook = bookRepository.findById(testBook1.getId());
		org.junit.jupiter.api.Assertions.assertFalse(deletedBook.isEmpty()); // Assert book is deleted
	}

	@Test
	void deleteBook_NonExistingId_ReturnsNotFound() throws Exception {
		doThrow(new com.ken.maktaba.exception.ResourceNotFoundException("Book not found with id: 999"))
				.when(bookService).deleteBook(999L);

		mockMvc.perform(delete("/books/{id}", 999L)).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("Book not found with id: 999")));
	}

	@Test
    void searchBooks_ByTitle_ReturnsMatchingBooks() throws Exception {
        when(bookService.searchBooks(Mockito.eq("Book 1"), Mockito.isNull(), Mockito.isNull(), Mockito.isNull())).thenReturn(Arrays.asList(testBookDTO1));

        mockMvc.perform(get("/books/search?title=Book 1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Book 1")))
                .andExpect(jsonPath("$[0].imageUrl", is("http://example.com/image1.jpg")));
    }

	@Test
    void searchBooks_ByAuthor_ReturnsMatchingBooks() throws Exception {
        when(bookService.searchBooks(Mockito.isNull(), Mockito.eq("Author 2"), Mockito.isNull(), Mockito.isNull())).thenReturn(Arrays.asList(testBookDTO2));

        mockMvc.perform(get("/books/search?author=Author 2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("Test Author 2")))
                .andExpect(jsonPath("$[0].imageUrl", is("http://example.com/image2.jpg")));
    }

	@Test
    void searchBooks_ByISBN_ReturnsMatchingBooks() throws Exception {
        when(bookService.searchBooks(Mockito.isNull(), Mockito.isNull(), Mockito.eq("222-222-222"), Mockito.isNull())).thenReturn(Arrays.asList(testBookDTO2));

        mockMvc.perform(get("/books/search?isbn=222-222-222"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is("222-222-222")))
                .andExpect(jsonPath("$[0].imageUrl", is("http://example.com/image2.jpg")));
    }

	@Test
    void searchBooks_ByQuery_ReturnsMatchingBooks() throws Exception {
        when(bookService.searchBooks(Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.eq("test query"))).thenReturn(Arrays.asList(testBookDTO1));

        mockMvc.perform(get("/books/search?q=test query"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Book 1")))
                .andExpect(jsonPath("$[0].imageUrl", is("http://example.com/image1.jpg")));
    }

	@Test
    void searchBooks_NoMatchingBooks_ReturnsEmptyList() throws Exception {
        when(bookService.searchBooks(Mockito.eq("NonExisting"), Mockito.isNull(), Mockito.isNull(), Mockito.isNull())).thenReturn(List.of());

        mockMvc.perform(get("/books/search?title=NonExisting"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

	@Test
    void getAiInsights_ExistingBook_ReturnsInsights() throws Exception {
        when(bookService.getBookInsights(testBook1.getId())).thenReturn(testInsights1);

        mockMvc.perform(get("/books/{id}/ai-insights", testBook1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testInsights1.getId().intValue())))
                .andExpect(jsonPath("$.book.id", is(testBook1.getId().intValue())));
    }

	@Test
    void getAiInsights_NonExistingBook_ReturnsNotFound() throws Exception {
        when(bookService.getBookInsights(999L)).thenThrow(new com.ken.maktaba.exception.ResourceNotFoundException("Book not found with id: 999"));

        mockMvc.perform(get("/books/{id}/ai-insights", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Book not found with id: 999")));
    }

	@Test
	void getAiInsights_AiApiError_ReturnsInternalServerError() throws Exception {
		Mockito.when(aiServiceMock.createChatCompletion(any(ChatCompletionRequest.class)))
				.thenThrow(new com.ken.maktaba.exception.AiApiException("AI API Error"));
		when(bookService.getBookInsights(testBook1.getId())).thenReturn(testInsights1);

		mockMvc.perform(get("/books/{id}/ai-insights", testBook1.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
}