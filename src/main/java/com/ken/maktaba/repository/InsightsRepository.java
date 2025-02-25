package com.ken.maktaba.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ken.maktaba.entity.Insights;

public interface InsightsRepository extends JpaRepository<Insights, Long> {

	Optional<Insights> findByBookId(Long bookId);
}