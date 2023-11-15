package io.jay.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainApplicationTests {

    @Autowired
    WebTestClient wtc;

    @Autowired
    BookRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Insert a new book")
    void test_create() {
        var actual = wtc.post()
                .uri("/create")
                .bodyValue(new NewBookRequest("New Book"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();


        assertThat(actual.getIsbn()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo("New Book");
    }

    @Test
    @DisplayName("Find book by ISBN")
    void test_book() {
        var saved = wtc.post()
                .uri("/create")
                .bodyValue(new NewBookRequest("New Book"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();


        var actual = wtc.get()
                .uri("/book/{isbn}", saved.getIsbn())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();


        assertThat(actual).isEqualTo(saved);
    }

    @Test
    @DisplayName("Find all books")
    void test_books() {
        var saved = wtc.post()
                .uri("/create")
                .bodyValue(new NewBookRequest("New Book"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();


        var actual = wtc.get()
                .uri("/books")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .returnResult()
                .getResponseBody();


        assertThat(actual).containsExactly(saved);
    }

    @Test
    @DisplayName("Update book")
    void test_update() {
        var saved = wtc.post()
                .uri("/create")
                .bodyValue(new NewBookRequest("New Book"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();


        var actual = wtc.put()
                .uri("/update", saved.getIsbn())
                .bodyValue(new UpdateBookRequest(saved.getIsbn(), "Updated Book"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();


        assertThat(actual.getIsbn()).isEqualTo(saved.getIsbn());
        assertThat(actual.getTitle()).isEqualTo("Updated Book");
    }

    @Test
    @DisplayName("Delete book")
    void test_delete() {
        var saved = wtc.post()
                .uri("/create")
                .bodyValue(new NewBookRequest("New Book"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();


        wtc.delete()
                .uri("/delete/{isbn}", saved.getIsbn())
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
