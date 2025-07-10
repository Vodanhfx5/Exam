package com.lms.exam.repository;

import com.lms.exam.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    Optional<ExamResult> findByExamIdAndUserId(Long examId, String userId);
}