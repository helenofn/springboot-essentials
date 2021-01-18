package br.com.curso.devdojospringboot.util;

import br.com.curso.devdojospringboot.requestbodies.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
    public static AnimePostRequestBody creatAnimePostRequestBody(){
        return AnimePostRequestBody.builder()
            .name(AnimeCreator.creatAnimeToBeSaved().getName())
            .build();
    }
}
