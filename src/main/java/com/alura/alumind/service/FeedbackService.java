package com.alura.alumind.service;

import com.alura.alumind.domain.dto.request.FeedbackRequest;
import com.alura.alumind.domain.dto.response.FeedbackResponse;
import com.alura.alumind.domain.dto.response.RequestedFeature;
import com.alura.alumind.domain.entity.FeedbackEntity;
import com.alura.alumind.domain.entity.MelhoriasFeedbackEntity;
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

        String prompt = FeedbackInterpreterPromptBuilder(feedbackRequest.getFeedbackText());

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
            return new FeedbackResponse(classification, new ArrayList<>(), "");
        }

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setSentiment(classification);
        feedbackEntity.setFeedbackText(feedbackRequest.getFeedbackText());
        FeedbackEntity savedFeedbackEntity = feedbackRepository.save(feedbackEntity);


        List<RequestedFeature> requestedFeatures = new ArrayList<>();
        for (int i = 1; i < responseList.size(); i += 2) {
            if (i + 1 < responseList.size()) {
                requestedFeatures.add(new RequestedFeature(responseList.get(i), responseList.get(i + 1)));
            }
        }

        List<MelhoriasFeedbackEntity> melhorias = requestedFeatures.stream()
                .map(feature -> MelhoriasFeedbackEntity.builder()
                        .code(feature.getCode())
                        .reason(feature.getReason())
                        .feedbackId(savedFeedbackEntity.getId())
                        .build())
                .collect(Collectors.toList());
        melhoriasFeedbackRepository.saveAll(melhorias);

        return new FeedbackResponse(classification, requestedFeatures, generateFeedbackResponse(feedbackRequest.getFeedbackText(), classification, requestedFeatures));
    }

    private String FeedbackInterpreterPromptBuilder(String feedbackText) {
        return "Você deve atuar como um interpretador de feedbacks para a AluMind, uma startup que oferece um aplicativo focado em bem-estar e saúde mental, proporcionando aos usuários acesso a meditações guiadas, sessões de terapia, e conteúdos educativos sobre saúde mental. " +
                "Você deverá classificar feedbacks como POSITIVO, NEGATIVO, INCONCLUSIVO ou SPAM. " +
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

    private String generateFeedbackResponse(String feedbackText, Sentiment sentiment, List<RequestedFeature> requestedFeatures) {

        if (feedbackText == null || feedbackText.trim().isEmpty() || sentiment == Sentiment.INCONCLUSIVO ) {
            return "Obrigado pelo seu feedback! Estamos sempre trabalhando para melhorar a experiência do usuário.";
        }

        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("Você deve gerar uma resposta personalizada para o feedback recebido sobre o Alumind, "
                        + "uma startup que oferece um aplicativo focado em bem-estar e saúde mental. Considere o sentimento do feedback (")
                .append(sentiment)
                .append(") ")
                .append("e as melhorias propostas para gerar uma resposta empática e útil. Nunca afirme que estamos trabalhando nas melhorias sugeridas. Apenas deixe claro que vamos pensar em implementá-las "
                        + "Sua resposta deve ser construtiva e agradecida. ");

        if (sentiment == Sentiment.NEGATIVO) {
            promptBuilder.append("O feedback propõe as seguintes melhorias: ");
            for (RequestedFeature feature : requestedFeatures) {
                promptBuilder.append(feature.getReason()).append(". ");
            }
        }

        if (sentiment == Sentiment.POSITIVO) {
            promptBuilder.append("Obrigado pelo seu feedback! Ficamos felizes pelo seu interesse em nos ajudar a melhorar o Alumind!. ");
        } else if (sentiment == Sentiment.NEGATIVO) {
            promptBuilder.append("Agradecemos por compartilhar suas sugestões para melhorias. ");
        } else {
            promptBuilder.append("Agradecemos pelo seu feedback. ");
        }

        promptBuilder.append("Feedback: ").append(feedbackText);

        String modelResponse = chatModel.call(promptBuilder.toString());

        return modelResponse != null && !modelResponse.trim().isEmpty()
                ? modelResponse
                : "Obrigado pelo seu feedback! Estamos sempre trabalhando para melhorar a experiência do usuário.";
    }

}
