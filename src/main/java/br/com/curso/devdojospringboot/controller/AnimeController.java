package br.com.curso.devdojospringboot.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.curso.devdojospringboot.domain.Anime;
import br.com.curso.devdojospringboot.requestbodies.AnimePostRequestBody;
import br.com.curso.devdojospringboot.requestbodies.AnimePutRequestBody;
import br.com.curso.devdojospringboot.services.AnimeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
public class AnimeController {
    
    //private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable){
        //log.info(dateUtil.formatLocalDateTimeDataBaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok().body(animeService.listAll(pageable));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll(){
        //log.info(dateUtil.formatLocalDateTimeDataBaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok().body(animeService.listAllNoPageamble());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id){
        //log.info(dateUtil.formatLocalDateTimeDataBaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok().body(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(value = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(required = false) String name){
        //log.info(dateUtil.formatLocalDateTimeDataBaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok().body(animeService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody),HttpStatus.CREATED);
       
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
