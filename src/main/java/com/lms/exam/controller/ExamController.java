package com.lms.exam.controller;

import com.lms.exam.dto.*;
import com.lms.exam.service.ExamServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {
    private final ExamServiceImpl service;

    public ExamController(ExamServiceImpl service) { this.service = service; }

    @Operation(summary = "Create a new exam")
    @PostMapping
    public ExamDto createExam(@RequestBody ExamDto examDto) {
        return service.createExam(examDto);
    }

    @Operation(summary = "Get all exams")
    @GetMapping
    public List<ExamDto> getAllExams() {
        return service.getAllExams();
    }

    @Operation(summary = "Get a specific exam by id")
    @GetMapping("/{id}")
    public ExamDto getExam(@PathVariable Long id) {
        return service.getExam(id);
    }

    @Operation(summary = "Get exam by lessonId")
    @GetMapping("/by-lesson/{lessonId}")
    public ExamDto getExamByLessonId(@PathVariable Long lessonId) {
        return service.getExamByLessonId(lessonId);
    }

    @Operation(summary = "Delete an exam")
    @DeleteMapping("/{id}")
    public void deleteExam(@PathVariable Long id) {
        service.deleteExam(id);
    }

    @Operation(summary = "Add a question to an exam")
    @PostMapping("/{examId}/questions")
    public QuestionDto addQuestion(@PathVariable Long examId, @RequestBody QuestionDto q) {
        return service.addQuestion(examId, q);
    }

    @Operation(summary = "Delete a question")
    @DeleteMapping("/questions/{questionId}")
    public void deleteQuestion(@PathVariable Long questionId) {
        service.deleteQuestion(questionId);
    }

    @Operation(summary = "Submit answers for an exam (single result per user)")
    @PostMapping("/{examId}/submit")
    public ExamResultDto submitExamResult(
            @PathVariable Long examId,
            @RequestParam String userId,
            @RequestBody List<Integer> answers
    ) {
        return service.submitExamResult(examId, userId, answers);
    }

    @Operation(summary = "Get a user's exam result")
    @GetMapping("/{examId}/result")
    public ExamResultDto getExamResult(
            @PathVariable Long examId,
            @RequestParam String userId
    ) {
        return service.getExamResult(examId, userId);
    }
}