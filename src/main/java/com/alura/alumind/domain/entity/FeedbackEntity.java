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
@Table(name = "feedback") // Nome da tabela no banco
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática do id
    private Long id;

    @Enumerated(EnumType.STRING) // Enum como string no banco
    private Sentiment sentiment; // Classificação do feedback (POSITIVO, NEGATIVO, etc.)

    private String feedbackText; // O próprio feedback
}
