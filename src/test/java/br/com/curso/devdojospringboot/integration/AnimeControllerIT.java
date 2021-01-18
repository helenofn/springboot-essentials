package br.com.curso.devdojospringboot.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.repositories.AnimeRepository;
import br.com.curso.devdojospringboot.requestbodies.AnimePostRequestBody;
import br.com.curso.devdojospringboot.util.AnimeCreator;
import br.com.curso.devdojospringboot.util.AnimePostRequestBodyCreator;
import br.com.curso.devdojospringboot.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)//Assim para cada método o banco é recriado
public class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AnimeRepository animeRepository;

    /*@LocalServerPort
    private int port;*/

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessFul(){
        Anime savedAnime = animeRepository.save(AnimeCreator.creatAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
            new ParameterizedTypeReference<PageableResponse<Anime>>(){}).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("List returns list of anime when successful")
    void list_ReturnsListOfAnimes_WhenSuccessFul(){
        Anime savedAnime = animeRepository.save(AnimeCreator.creatAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Anime>>(){}).getBody();

        Assertions.assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return anime when successful")
    void findById_ReturnsAnime_WhenSuccessFul(){
        Anime savedAnime = animeRepository.save(AnimeCreator.creatAnimeToBeSaved());

        Long expectedId = savedAnime.getId();
        
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();     
        Assertions.assertThat(anime.getId())
            .isNotNull()
            .isEqualTo(expectedId);

    }

    @Test
    @DisplayName("FindByName return list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessFul(){

        Anime savedAnime = animeRepository.save(AnimeCreator.creatAnimeToBeSaved());

        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);
        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>(){}).getBody();

        Assertions.assertThat(animes)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return an empty list of anime when animes not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimesNotFound(){

        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=asdsada", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>(){}).getBody();

        Assertions.assertThat(animes)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnsAnime_WhenSuccessFul(){        
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.creatAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes/", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessFul(){   
        Anime savedAnime = animeRepository.save(AnimeCreator.creatAnimeToBeSaved());
        savedAnime.setName("new name");

       
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/",HttpMethod.PUT, new HttpEntity<>(savedAnime),Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessFul(){   
        Anime savedAnime = animeRepository.save(AnimeCreator.creatAnimeToBeSaved());
       
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",HttpMethod.DELETE, null,Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
