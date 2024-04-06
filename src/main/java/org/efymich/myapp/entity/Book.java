package org.efymich.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_seq")
    @SequenceGenerator(name = "books_seq", sequenceName = "BOOKS_SEQ",allocationSize = 1)
    Long bookId;

    String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;

    String genre;

    @Column(name = "publication_year")
    Long publicationYear;

    @OneToMany(mappedBy = "book")
    List<Report> report;
}
