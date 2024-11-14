package com.alura.alumind.service;

import com.alura.alumind.domain.dto.request.FeedbackRequest;
import com.alura.alumind.domain.dto.response.FeedbackResponse;
import com.alura.alumind.domain.dto.response.RequestedFeature;
import com.alura.alumind.domain.entity.Feedback;
import com.alura.alumind.domain.entity.MelhoriasFeedback;
import com.alura.alumind.domain.enumeration.Sentiment;
import com.alura.alumind.repository.FeedbackRepository;
import com.alura.alumind.repository.MelhoriasFeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);

    @Autowired
    private AzureOpenAiChatModel chatModel;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private MelhoriasFeedbackRepository melhoriasFeedbackRepository;

    public FeedbackResponse FeedbackInterpreter(FeedbackRequest feedbackRequest) {

        String prompt = buildPrompt(feedbackRequest.getFeedbackText());

        String modelResponse;
        try {
            modelResponse = chatModel.call(prompt);
        } catch (Exception e) {
            logger.error("Erro ao chamar o modelo de linguagem", e);
            throw new RuntimeException("Erro ao processar o feedback com o modelo de linguagem", e);
        }
        logger.info("Resposta do modelo: {}", modelResponse);

        List<String> responseList = modelResponse != null ? Arrays.asList(modelResponse.split(",")) : new ArrayList<>();

        if (responseList.isEmpty()) {
            throw new IllegalArgumentException("Resposta do modelo é inválida");
        }

        Sentiment classification = classifyResponse(responseList.get(0));

        if (classification.equals(Sentiment.SPAM)) {
            return new FeedbackResponse(classification, new ArrayList<>());
        }

        // Criar a entidade Feedback
        Feedback feedback = new Feedback();
        feedback.setSentiment(classification);
        feedback.setFeedbackText(feedbackRequest.getFeedbackText());

        // Persistir Feedback
        Feedback savedFeedback = feedbackRepository.save(feedback);

        List<RequestedFeature> requestedFeatures = IntStream.range(1, responseList.size() / 2)
                .mapToObj(i -> new RequestedFeature(responseList.get(i * 2 - 1), responseList.get(i * 2)))
                .collect(Collectors.toList());

        List<MelhoriasFeedback> melhorias = requestedFeatures.stream()
                .map(feature -> MelhoriasFeedback.builder()
                        .code(feature.getCode())
                        .reason(feature.getReason())
                        .feedbackId(savedFeedback.getId())
                        .build())
                .collect(Collectors.toList());


        melhoriasFeedbackRepository.saveAll(melhorias);

        return new FeedbackResponse(classification, requestedFeatures);
    }

    private String buildPrompt(String feedbackText) {
        return "Você deve atuar como um interpretador de feedbacks para a AluMind, uma startup que oferece um aplicativo focado em bem-estar e saúde mental, proporcionando aos usuários acesso a meditações guiadas, sessões de terapia, e conteúdos educativos sobre saúde mental. " +
                "Você deverá classificar feedbacks como POSITIVO, NEUTRO, NEGATIVO ou SPAM. " +
                "SPAM deve ser usado quando o feedback contiver conteúdo agressivo ou não fizer sentido dentro do contexto de saúde mental e bem-estar. Não sugira melhorias caso a classificação for de SPAM ou se o feedback por muito simples ou não construtivo. " +
                "Em seguida, informe quais melhorias devem ser feitas para satisfazer completamente o feedback do usuário juntamente a uma TAG que represente resumidamente a melhoria. " +
                "Sua resposta deve seguir exatamente o padrão: 'Classificação, TAG 1, melhoria 1, TAG 2, melhoria 2, TAG 3, melhoria 3, TAG 4, melhoria 4'. " +
                "Exemplo: 'NEGATIVO, EDITOR_PERFIL, Implementar funcionalidade de edição de perfil, TUTORIAL_EDITOR_PERFIL, Adicionar tutorial sobre edição de perfil, PERSONALIZACAO_PERFIL, Permitir personalização do perfil'. " +
                "Exemplo: 'SPAM,'" +
                "Exemplo: 'POSITIVO, Nenhuma melhoria sugerida'. " +
                "Escreva quantas melhorias achar necessário. Em seguida o feedback:" +
                "FEEDBACK: \"" + feedbackText + "\".";
    }

    private Sentiment classifyResponse(String response) {

        return Arrays.stream(Sentiment.values())
                .filter(sentiment -> sentiment.name().equals(response))
                .findFirst()
                .orElse(Sentiment.SPAM);
    }
}
