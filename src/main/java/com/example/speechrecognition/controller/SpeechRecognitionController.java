package com.example.speechrecognition.controller;

import com.example.speechrecognition.service.SpeechRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/speech")
public class SpeechRecognitionController {

    @Autowired
    private SpeechRecognitionService speechRecognitionService;

    @PostMapping("/transcribe")
    public String transcribe(@RequestParam("file") MultipartFile file) {
        try {
            byte[] audioData = file.getBytes();
            return speechRecognitionService.transcribe(audioData);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error during transcription: " + e.getMessage();
        }
    }
}
