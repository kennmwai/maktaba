package com.ken.maktaba.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ken.maktaba.dto.ChatCompletionRequest;
import com.ken.maktaba.dto.MessageDto;
import com.ken.maktaba.entity.Book;
import com.ken.maktaba.entity.InsightStatus;
import com.ken.maktaba.entity.Insights;
import com.ken.maktaba.exception.AiApiException;
import com.ken.maktaba.exception.ResourceNotFoundException;
import com.ken.maktaba.repository.BookRepository;
import com.ken.maktaba.repository.InsightsRepository;
import com.ken.maktaba.util.AIService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookServiceImpl implements BookService {

	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	private final BookRepository bookRepository;
	private final InsightsRepository insightsRepository;
	private final AIService aiService;

	public BookServiceImpl(BookRepository bookRepository, InsightsRepository insightsRepository, AIService aiService) {
		this.bookRepository = bookRepository;
		this.insightsRepository = insightsRepository;
		this.aiService = aiService;
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
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book getBookById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
	}

	@Override
	public List<Book> searchBooks(String title, String author, String isbn, String query) {
		if (query != null && !query.isEmpty()) {
			return bookRepository.searchBooksByKeyword(query);
		} else if (isbn != null && !isbn.isEmpty()) {
			return bookRepository.findByIsbn(isbn);
		} else if (title != null || author != null) {
			return bookRepository.searchBooksByTitleAndAuthor(title, author);
		} else {
			return getAllBooks();
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

	@Override
	public Insights getBookInsights(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

		Insights insights = insightsRepository.findByBookId(bookId).orElseGet(() -> {
			Insights newInsights = new Insights();
			newInsights.setBook(book);
			return insightsRepository.saveAndFlush(newInsights); // Ensure immediate persistence
		});

		if (insights.getAiInsights() == null) {
			generateAIInsightsAsync(insights.getId());
		}

		return insights;
	}

	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW) // Isolate async transaction
	public CompletableFuture<Void> generateAIInsightsAsync(Long insightsId) {
		try {
			Insights insights = insightsRepository.findById(insightsId)
					.orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + insightsId));

			Book book = insights.getBook();

			String prompt = generatePrompt(PROMPT_TEMPLATE, book.getTitle(), book.getAuthor(), book.getDescription());

			// Call AI API
			ChatCompletionRequest request = new ChatCompletionRequest();
			request.setMessages(
					List.of(new MessageDto("system", "You are a helpful assistant."), new MessageDto("user", prompt)));

			String aiResponse = aiService.createChatCompletion(request);

			// Update and persist Insights
			insights.setAiInsights(aiResponse.trim());
			insights.setStatus(InsightStatus.SUCCESS);
			insights.setErrorMessage(null);
			insightsRepository.save(insights);

		} catch (EntityNotFoundException e) {
			logger.error("Failed to process insights ID {}: {}", insightsId, e.getMessage());
		} catch (AiApiException e) {
			handleAIError(insightsId, "API Error: " + e.getMessage());
		} catch (Exception e) {
			handleAIError(insightsId, "Internal Error: " + e.getMessage());
		}

		return CompletableFuture.completedFuture(null);
	}

	private static final String PROMPT_TEMPLATE = "Generate a concise summary (around 75 words) for the book: \"%1$s\" by %2$s. Book description: %3$s. The summary should:\\n- Briefly introduce the main character and their central conflict.\\n- Mention the setting or time period.\\n- Hint at the main themes of the book.\\n- End with a hook to make the reader want to learn more.";

	private String generatePrompt(String promptTemplate, String title, String author, String description) {
		return String.format(promptTemplate, title, author, description);
	}

	private void handleAIError(Long insightsId, String errorMessage) {
		logger.error("Error generating AI insights for insights ID: {}. Error message: {}", insightsId, errorMessage);
		Insights insights = insightsRepository.findById(insightsId).orElse(null);
		if (insights != null) {
			insights.setStatus(InsightStatus.FAILED);
			insights.setErrorMessage(errorMessage);
			insightsRepository.save(insights);
		}
	}
}
