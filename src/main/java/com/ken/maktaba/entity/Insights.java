package com.ken.maktaba.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "insights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insights {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String aiInsights;

	@Enumerated(EnumType.STRING)
	private InsightStatus status;

	@Column(columnDefinition = "TEXT")
	private String errorMessage;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", unique = true)
	private Book book;
}