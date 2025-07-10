package com.lms.exam.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exam exam;

    private String userId;
    private Integer score;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "exam_result_answers",
            joinColumns = @JoinColumn(name = "exam_result_id")
    )
    @Column(name = "answer")
    private List<Integer> answers;

    private LocalDateTime submittedAt;
}
