package br.com.curso.devdojospringboot.requestbodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimePutRequestBody {

    private Long id;
    private String name;
}
