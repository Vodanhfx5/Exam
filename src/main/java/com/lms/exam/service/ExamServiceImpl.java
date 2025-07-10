package com.lms.exam.service;

import com.lms.exam.dto.*;
import com.lms.exam.model.*;
import com.lms.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultRepository examResultRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository, ExamResultRepository examResultRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.examResultRepository = examResultRepository;
    }

    private ExamDto toExamDto(Exam exam) {
        ExamDto dto = new ExamDto();
        dto.setId(exam.getId());
        dto.setTitle(exam.getTitle());
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setLocation(exam.getLocation());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dto.setTimeslot(exam.getTimeslot() != null ? exam.getTimeslot().format(formatter) : null);
        dto.setQuestions(
                exam.getQuestions() != null
                        ? exam.getQuestions().stream().map(this::toQuestionDto).collect(Collectors.toList())
                        : List.of()
        );
        dto.setLessonId(exam.getLessonId());
        return dto;
    }

    private QuestionDto toQuestionDto(Question q) {
        QuestionDto dto = new QuestionDto();
        dto.setId(q.getId());
        dto.setText(q.getText());
        dto.setChoices(q.getChoices());
        dto.setCorrectAnswerIndex(q.getCorrectAnswerIndex());
        return dto;
    }

    private ExamResultDto toExamResultDto(ExamResult result) {
        return new ExamResultDto(
                result.getId(),
                result.getExam().getId(),
                result.getUserId(),
                result.getScore(),
                result.getAnswers(),
                result.getSubmittedAt()
        );
    }

    @Override
    public ExamDto createExam(ExamDto dto) {
        Exam exam = new Exam();
        exam.setTitle(dto.getTitle());
        exam.setDurationMinutes(dto.getDurationMinutes());
        exam.setLocation(dto.getLocation());
        exam.setLessonId(dto.getLessonId());
        if (dto.getTimeslot() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            exam.setTimeslot(LocalDateTime.parse(dto.getTimeslot(), formatter));
        }
        if (dto.getQuestions() != null) {
            List<Question> questions = dto.getQuestions().stream().map(qdto -> {
                Question q = new Question();
                q.setText(qdto.getText());
                q.setChoices(qdto.getChoices());
                q.setCorrectAnswerIndex(qdto.getCorrectAnswerIndex());
                q.setExam(exam);
                return q;
            }).collect(Collectors.toList());
            exam.setQuestions(questions);
        }
        Exam saved = examRepository.save(exam);
        return toExamDto(saved);
    }

    @Override
    public ExamDto getExam(Long id) {
        return examRepository.findById(id).map(this::toExamDto).orElseThrow();
    }

    @Override
    public List<ExamDto> getAllExams() {
        return examRepository.findAll().stream().map(this::toExamDto).collect(Collectors.toList());
    }

    @Override
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    @Override
    public QuestionDto addQuestion(Long examId, QuestionDto qdto) {
        Exam exam = examRepository.findById(examId).orElseThrow();
        Question q = new Question();
        q.setText(qdto.getText());
        q.setChoices(qdto.getChoices());
        q.setCorrectAnswerIndex(qdto.getCorrectAnswerIndex());
        q.setExam(exam);
        Question saved = questionRepository.save(q);
        return toQuestionDto(saved);
    }

    @Override
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    @Transactional
    public ExamResultDto submitExamResult(Long examId, String userId, List<Integer> userAnswers) {
        Exam exam = examRepository.findById(examId).orElseThrow();
        Optional<ExamResult> existing = examResultRepository.findByExamIdAndUserId(examId, userId);
        if (existing.isPresent()) {
            throw new IllegalStateException("User has already submitted result for this exam");
        }

        List<Question> questions = exam.getQuestions();
        List<Integer> correctAnswers = questions.stream().map(Question::getCorrectAnswerIndex).collect(Collectors.toList());
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (i < userAnswers.size() && userAnswers.get(i).equals(correctAnswers.get(i))) score++;
        }

        ExamResult result = new ExamResult();
        result.setExam(exam);
        result.setUserId(userId);
        result.setScore(score);
        result.setAnswers(userAnswers);
        result.setSubmittedAt(LocalDateTime.now());

        return toExamResultDto(examResultRepository.save(result));
    }

    @Override
    public ExamResultDto getExamResult(Long examId, String userId) {
        ExamResult result = examResultRepository.findByExamIdAndUserId(examId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No exam result for this user and exam"));
        return toExamResultDto(result);
    }

    @Override
    public ExamDto getExamByLessonId(Long lessonId) {
        Exam exam = examRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new RuntimeException("Exam not found for lessonId: " + lessonId));
        return toExamDto(exam);
    }
}