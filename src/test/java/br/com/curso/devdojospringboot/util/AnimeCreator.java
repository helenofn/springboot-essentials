package br.com.curso.devdojospringboot.util;

import br.com.curso.devdojospringboot.domain.Anime;

public class AnimeCreator {
    
    public static Anime creatAnimeToBeSaved(){
        return Anime.builder().name("Test Anime").build();
    }

    public static Anime creatValidAnime(){
        return Anime.builder()
            .name("Test Anime")
            .id(1L)
            .build();
    }

    public static Anime creatValidaUpdatedAnime(){
        return Anime.builder()
            .name("Test Anime - diferent name")
            .id(1L)
            .build();
    }
}
