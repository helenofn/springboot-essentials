package br.com.curso.devdojospringboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.curso.devdojospringboot.domain.Anime;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long>{
    List<Anime> findByName(String name);
}
