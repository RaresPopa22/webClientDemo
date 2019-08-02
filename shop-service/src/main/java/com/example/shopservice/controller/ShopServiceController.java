package com.example.shopservice.controller;

import com.example.shopservice.document.Shop;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@RestController
@RequestMapping("/shops")
public class ShopServiceController {

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Shop> getAll() {
    return Flux.<Shop>generate(syncSink -> syncSink.next(new Shop("ShopWannabe Nr." + getRandomNumber(), LocalDateTime.now())))
      .delayElements(Duration.ofSeconds(1));
  }

  @PostMapping
  public Mono<Shop> save(@RequestBody final Mono<Shop> shop) {
    return shop.flatMap(shopObj -> {
      shopObj.setCreatedAt(LocalDateTime.now());
      return Mono.just(shopObj);
    });
  }

  private long getRandomNumber() {
    return Math.round(Math.random() * 100);
  }
}
