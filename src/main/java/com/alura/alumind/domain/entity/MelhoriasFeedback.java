package com.alura.alumind.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "melhorias_feedback") // Nome da tabela no banco
public class MelhoriasFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática do id
    private Long id;

    private String code; // TAG do feedback
    private String reason; // Explicação do feedback

    private Long feedbackId; // ID do feedback completo associado
}
