package br.com.curso.devdojospringboot.repositories;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.util.AnimeCreator;

@DataJpaTest
@DisplayName("Tests for anime repository")
class AnimeRepositoryTest {
    
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persists anime when successful")
    void save_PersistAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.creatAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save update anime when successful")
    void save_Update_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.creatAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        savedAnime.setName("updated name");
        Anime updatedAnime = this.animeRepository.save(savedAnime);

        Assertions.assertThat(updatedAnime).isNotNull();
        Assertions.assertThat(updatedAnime.getId()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("Delete remove anime when successful")
    void delete_Remove_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.creatAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(savedAnime);
        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list  on anime when successful")
    void findByName_ReturnListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.creatAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        String name = savedAnime.getName();
        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes)
                .isNotEmpty()
                .contains(savedAnime);
    }

    @Test
    @DisplayName("Find by name returns empty list when no anime is found")
    void findByName_ReturnEmptyList_WhenAnimeIsNotFound(){
        List<Anime> animes = this.animeRepository.findByName("Nao exite asdjkalslc");
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintValidationException when name is empty")
    void save_ThrowConstraintValidationException_WhenNameIsEmpty(){
        Anime animeToBeSaved = new Anime();
        /*Assertions.assertThatThrownBy(() -> this.animeRepository.save(animeToBeSaved))
            .isInstanceOf(ConstraintViolationException.class);*/
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> this.animeRepository.save(animeToBeSaved))
            .withMessageContaining("The anime name can not be empty");
    }

    
}
