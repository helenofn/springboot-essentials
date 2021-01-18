package br.com.curso.devdojospringboot.util;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.requestbodies.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody creatAnimePutRequestBody(){

        Anime anime = AnimeCreator.creatValidaUpdatedAnime();

        return AnimePutRequestBody.builder()
            .name(anime.getName())
            .id(anime.getId())
            .build();
    }
}
