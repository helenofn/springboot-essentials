package br.com.curso.devdojospringboot.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.exceptions.BadRequestException;
import br.com.curso.devdojospringboot.mapper.AnimeMapper;
import br.com.curso.devdojospringboot.repositories.AnimeRepository;
import br.com.curso.devdojospringboot.requestbodies.AnimePostRequestBody;
import br.com.curso.devdojospringboot.requestbodies.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class AnimeService {
    
    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

    public List<Anime> listAllNoPageamble(){
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(Long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    @Transactional
    public Anime save(AnimePostRequestBody animePostRequestBody){     
        return animeRepository.save(AnimeMapper.toAnime(animePostRequestBody));
    }

    public void delete(Long id){
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody){
        Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.toAnime(animePutRequestBody);
        savedAnime.setName(anime.getName());

        //delete(anime.getId());
        log.info("Updated anime: {}",savedAnime);
        animeRepository.save(savedAnime);
    }
}
