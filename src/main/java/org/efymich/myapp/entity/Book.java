package org.efymich.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "books")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_seq")
    @SequenceGenerator(name = "books_seq", sequenceName = "BOOKS_SEQ",allocationSize = 1)
    Long bookId;

    @ToString.Exclude
    String title;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;

    @ToString.Exclude
    String genre;

    @ToString.Exclude
    @Column(name = "publication_year")
    Long publicationYear;

    @ToString.Exclude
    @OneToMany(mappedBy = "book")
    List<Report> report;
}
