package com.alura.alumind.domain.entity;

import com.alura.alumind.domain.enumeration.Sentiment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feedback")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    private String feedbackText;
}
