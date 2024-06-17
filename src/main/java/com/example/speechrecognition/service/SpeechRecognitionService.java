package com.example.speechrecognition.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpeechRecognitionService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/audio/transcriptions";

    public String transcribe(byte[] audioData) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "audio.wav",
                        RequestBody.create(audioData, MediaType.parse("audio/wav")))
                .addFormDataPart("model", "whisper-1") // Specify the model parameter
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode resultNode = mapper.readTree(responseBody);

            // Check if the transcription result exists and extract it
            if (resultNode.has("text")) {
                return resultNode.get("text").asText();
            } else {
                return "Transcription result not found in response.";
            }
        }
    }
}
