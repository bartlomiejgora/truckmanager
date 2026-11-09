package io.github.bartlomiejgora.drivers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controler {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Ok");

    }
}
