package com.dowglasmaia.bookstore.controllers;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/bookstore")
public class BookStoreAssistantController {

    @Autowired
    private OpenAiChatClient openAiChatClient;

    @GetMapping("/informations")
    public String bookstoreChat(
            @RequestParam(value = "message",
                    defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {

        return openAiChatClient.call(message);
    }

    @GetMapping("/informations/prompt")
    public ChatResponse bookstoreChatPrompt(
            @RequestParam(value = "message",
                    defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {

        var messageResponse = new Prompt(message);

        return openAiChatClient.call(messageResponse);
    }

    @GetMapping("/informations/reviews")
    public String bookStoreReview(@RequestParam(value = "book", defaultValue = "Dom Quixote") String book) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                 Por favor, me forneça um breve resumo do livro {book}
                e também a biografia de seu autor.
                """);

        promptTemplate.add("book", book);
        return this.openAiChatClient.call(promptTemplate
                .create()).getResult().getOutput().getContent();
    }

    @GetMapping("/stream/informations")
    public Flux<String> bookstoreChatStream(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {

        return openAiChatClient.stream(message);
    }

    @GetMapping("/stream/informations/prompt")
    public Flux<ChatResponse> bookstoreChatStreamPrompt(@RequestParam(value = "message",
            defaultValue = "Quais são os livros best sellers dos ultimos anos?") String message) {

        var messageResponse = new Prompt(message);

        return openAiChatClient.stream(messageResponse);
    }

}
