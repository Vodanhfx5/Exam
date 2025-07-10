package com.lms.exam.repository;

import com.lms.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByLessonId(Long lessonId);
}