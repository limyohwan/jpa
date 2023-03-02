package com.example.datajpa.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {
    

    private final SseEmitters sseEmitters;

    @GetMapping(value = "/connect", produces =  "text/event-stream")
    public ResponseEntity<SseEmitter> connect(){
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);
        try{
            log.info("connect sse emitter");
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/count")  
    public ResponseEntity<Void> count() {  
        log.info("start count");
        sseEmitters.count();  
        return ResponseEntity.ok().build();  
    }  
}
