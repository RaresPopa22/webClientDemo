package com.example.shopclient.controller;

import com.example.shopclient.document.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ShopClientController {

  private final WebClient client;

  @GetMapping(value = "/shops", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Shop> getAll() {
    return client.get().uri("/shops")
      .accept(MediaType.TEXT_EVENT_STREAM)
      .retrieve()
      .bodyToFlux(Shop.class)
      .filter(shopRetrieved -> getOnlyEvenShops(shopRetrieved));
  }

  private boolean getOnlyEvenShops(final Shop shopRetrieved) {
    final String name = shopRetrieved.getName();
    final String number = name.replaceAll("\\D+", "");

    return Integer.parseInt(number) % 2 == 0;
  }

  @PostMapping
  public Mono<Shop> save(@RequestBody final Shop shop) {
    final Mono<Shop> shopMono = Mono.just(shop);

    return client.post().uri("/shops")
      .contentType(MediaType.APPLICATION_JSON)
      .body(shopMono, Shop.class)
      .retrieve()
      .bodyToMono(Shop.class);
  }
}
