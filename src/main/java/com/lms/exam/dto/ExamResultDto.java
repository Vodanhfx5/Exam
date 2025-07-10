package com.lms.exam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDto {
    private Long id;
    private Long examId;
    private String userId;
    private Integer score;
    private List<Integer> answers;
    private LocalDateTime submittedAt;
}
