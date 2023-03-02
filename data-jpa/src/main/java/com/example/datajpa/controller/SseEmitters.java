package com.example.datajpa.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SseEmitters {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    SseEmitter add(SseEmitter emitter){
        this.emitters.add(emitter);
        log.info("new emitter added: {}", emitter);  
        log.info("emitter list size: {}", emitters.size());  
        emitter.onCompletion(() -> {
            log.info("onCompletion callback"); 
            this.emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            log.info("onTimeout callback");  
            emitter.complete();
        });

        return emitter;
    }

    public void count() {
        emitters.forEach(emitter -> {  
            try {  
                emitter.send(SseEmitter.event()  
                        .name("reload")  
                        .data("reload"));  
            } catch (IOException e) {  
                throw new RuntimeException(e);  
            }  
        });  
    }
}
