package br.com.curso.devdojospringboot.mapper;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.requestbodies.AnimePostRequestBody;
import br.com.curso.devdojospringboot.requestbodies.AnimePutRequestBody;

public class AnimeMapper {

    public static Anime toAnime(AnimePostRequestBody animePostRequestBody){
        return Anime.builder().name(animePostRequestBody.getName()).build();
    }

    public static Anime toAnime(AnimePutRequestBody animePutRequestBody){
        return Anime.builder()
                        .id(animePutRequestBody.getId())
                        .name(animePutRequestBody.getName()).build();    
    }
}
