package br.com.curso.devdojospringboot.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.exceptions.BadRequestException;
import br.com.curso.devdojospringboot.repositories.AnimeRepository;
import br.com.curso.devdojospringboot.util.AnimeCreator;
import br.com.curso.devdojospringboot.util.AnimePostRequestBodyCreator;
import br.com.curso.devdojospringboot.util.AnimePutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
public class AnimeServiceTest {
    //Quando quero testar a classe em si
    @InjectMocks
    private AnimeService animeService;

    //Todas as classes de dentro da classe que será testada
    @Mock
    private AnimeRepository animeRepositoryMock;


    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.creatValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll())
            .thenReturn(List.of(AnimeCreator.creatValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(AnimeCreator.creatValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
            .thenReturn(List.of(AnimeCreator.creatValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
            .thenReturn(AnimeCreator.creatValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("listAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessFul(){
        String expectedName = AnimeCreator.creatValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(0, 10));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("listAllNoPageamble returns list of anime when successful")
    void listAllNoPageamble_ReturnsListOfAnimes_WhenSuccessFul(){
        String expectedName = AnimeCreator.creatValidAnime().getName();
        List<Anime> animes = animeService.listAllNoPageamble();

        Assertions.assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException return anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessFul(){
        Long expectedId = AnimeCreator.creatValidAnime().getId();
        
        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(anime).isNotNull();     
        Assertions.assertThat(anime.getId())
            .isNotNull()
            .isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound(){

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());
        
        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1L));
    }

    @Test
    @DisplayName("FindByName return list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessFul(){
        String expectedName = AnimeCreator.creatValidAnime().getName();
        List<Anime> animes = animeService.findByName("Test Anime");

        Assertions.assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return an empty list of anime when animes not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimesNotFound(){

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
        .thenReturn(Collections.emptyList());

        List<Anime> animes = animeService.findByName("Test Anime");

        Assertions.assertThat(animes)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnsAnime_WhenSuccessFul(){        
        Anime anime = animeService.save(AnimePostRequestBodyCreator.creatAnimePostRequestBody());

        Assertions.assertThat(anime)
            .isNotNull()
            .isEqualTo(AnimeCreator.creatValidAnime());
    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessFul(){   
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.creatAnimePutRequestBody()))
            .doesNotThrowAnyException();
       
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessFul(){   
        Assertions.assertThatCode(() -> animeService.delete(1L))
            .doesNotThrowAnyException();
       
    }
}
