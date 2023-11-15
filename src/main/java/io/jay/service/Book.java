package io.jay.service;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Book {

    @Id
    private String isbn;
    private String title;

    public Book(String title) {
        this.isbn = UUID.randomUUID().toString();
        this.title = title;
    }
}
