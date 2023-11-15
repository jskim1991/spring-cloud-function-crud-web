package io.jay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    @Bean
    public Supplier<List<Book>> books() {
        return () -> repository.findAll();
    }

    @Bean
    public Function<String, Book> book() {
        return isbn -> repository.findById(isbn).get();
    }

    @Bean
    public Function<NewBookRequest, Book> create() {
        return request -> repository.save(new Book(request.title()));
    }

    @Bean
    public Function<UpdateBookRequest, Book> update() {
        return request -> {
            var book = repository.findById(request.isbn()).get();
            book.setTitle(request.title());
            return repository.save(book);
        };
    }

    @Bean
    public Consumer<String> delete() {
        return isbn -> repository.deleteById(isbn);
    }
}
