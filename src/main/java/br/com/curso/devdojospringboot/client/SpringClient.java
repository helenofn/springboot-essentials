package br.com.curso.devdojospringboot.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.curso.devdojospringboot.domain.Anime;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {
    public static void mainTestRestTemplate(String [] args){

        //Exemplos de GET
        ResponseEntity<Anime> animeEntity = new RestTemplate().getForEntity("http://localhost:8081/animes/{id}", Anime.class, 10);
        log.info(animeEntity);

        Anime animeObj = new RestTemplate().getForObject("http://localhost:8081/animes/{id}", Anime.class, 10);
        log.info(animeObj);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8081/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));

        ResponseEntity<List<Anime>> listAnimes = new RestTemplate().exchange("http://localhost:8081/animes/all",HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>(){});
        log.info(listAnimes.getBody());

        //Exemplos de POST
        /*Anime anime1 = Anime.builder().name("Anime1").build();
        Anime anime1Response = new RestTemplate().postForObject("http://localhost:8081/animes/", anime1, Anime.class);
        log.info("Saved anime: {}",anime1Response);*/

        //Exemplos de POST
        Anime anime1 = Anime.builder().name("Anime1").build();
        ResponseEntity<Anime> anime1ResponseSaved = new RestTemplate().exchange("http://localhost:8081/animes/", HttpMethod.POST, new HttpEntity<>(anime1,createJsonHeader()), Anime.class);
        log.info("Saved anime: {}",anime1ResponseSaved.getBody());

        //Exmplos de PUT
        Anime animeToBeUpdated = anime1ResponseSaved.getBody();
        animeToBeUpdated.setName("Modified name");
        ResponseEntity<Void> anime1ResponseUpadated = new RestTemplate().exchange("http://localhost:8081/animes/", HttpMethod.PUT, new HttpEntity<>(animeToBeUpdated,createJsonHeader()), Void.class);
        log.info("Updaded anime: {}",anime1ResponseUpadated);

        //Exemplo de DELETE
        ResponseEntity<Void> anime1ResponseDelete = new RestTemplate().exchange("http://localhost:8081/animes/{id}", HttpMethod.DELETE, null, Void.class, animeToBeUpdated.getId());
        log.info("Deleted anime: {}",anime1ResponseDelete);
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
