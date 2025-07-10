package com.lms.exam.service;

import com.lms.exam.dto.*;
import java.util.List;

public interface ExamService {
    ExamDto createExam(ExamDto dto);
    ExamDto getExam(Long id);
    List<ExamDto> getAllExams();
    void deleteExam(Long id);
    QuestionDto addQuestion(Long examId, QuestionDto qdto);
    void deleteQuestion(Long questionId);
    ExamResultDto submitExamResult(Long examId, String userId, List<Integer> userAnswers);
    ExamResultDto getExamResult(Long examId, String userId);
    ExamDto getExamByLessonId(Long lessonId);
}