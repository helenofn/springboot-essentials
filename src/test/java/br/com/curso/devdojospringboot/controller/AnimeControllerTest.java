package br.com.curso.devdojospringboot.controller;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.requestbodies.AnimePostRequestBody;
import br.com.curso.devdojospringboot.requestbodies.AnimePutRequestBody;
import br.com.curso.devdojospringboot.services.AnimeService;
import br.com.curso.devdojospringboot.util.AnimeCreator;
import br.com.curso.devdojospringboot.util.AnimePostRequestBodyCreator;
import br.com.curso.devdojospringboot.util.AnimePutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    //Quando quero testar a classe em si
    @InjectMocks
    private AnimeController animeController;

    //Todas as classes de dentro da classe que será testada
    @Mock
    private AnimeService animeServiceMock;


    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.creatValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
            .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNoPageamble())
            .thenReturn(List.of(AnimeCreator.creatValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
            .thenReturn(AnimeCreator.creatValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
            .thenReturn(List.of(AnimeCreator.creatValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
            .thenReturn(AnimeCreator.creatValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessFul(){
        String expectedName = AnimeCreator.creatValidAnime().getName();
        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("List returns list of anime when successful")
    void list_ReturnsListOfAnimes_WhenSuccessFul(){
        String expectedName = AnimeCreator.creatValidAnime().getName();
        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("FindById return anime when successful")
    void findById_ReturnsAnime_WhenSuccessFul(){
        Long expectedId = AnimeCreator.creatValidAnime().getId();
        
        Anime anime = animeController.findById(1L).getBody();

        Assertions.assertThat(anime).isNotNull();     
        Assertions.assertThat(anime.getId())
            .isNotNull()
            .isEqualTo(expectedId);

    }

    @Test
    @DisplayName("FindByName return list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessFul(){
        String expectedName = AnimeCreator.creatValidAnime().getName();
        List<Anime> animes = animeController.findByName("Test Anime").getBody();

        Assertions.assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return an empty list of anime when animes not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimesNotFound(){

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
        .thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName("Test Anime").getBody();

        Assertions.assertThat(animes)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnsAnime_WhenSuccessFul(){        
        Anime anime = animeController.save(AnimePostRequestBodyCreator.creatAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime)
            .isNotNull()
            .isEqualTo(AnimeCreator.creatValidAnime());
    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessFul(){   
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.creatAnimePutRequestBody()).getBody())
            .doesNotThrowAnyException();
        
        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.creatAnimePutRequestBody());
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
       
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessFul(){   
        Assertions.assertThatCode(() -> animeController.delete(1L))
            .doesNotThrowAnyException();
        
        ResponseEntity<Void> entity = animeController.delete(1L);
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
       
    }

}