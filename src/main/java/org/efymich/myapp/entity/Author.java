package org.efymich.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.efymich.myapp.enums.Nationalities;

import java.util.Set;

@Entity
@Table(name = "authors")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authors_seq")
    @SequenceGenerator(name = "authors_seq", sequenceName = "AUTHORS_SEQ")
    @Column(name = "author_id")
    Long authorId;

    @Column(name = "author_name")
    String authorName;

    @Enumerated(EnumType.STRING)
    Nationalities nationality;

    @OneToMany(mappedBy = "author")
    Set<Book> books;
}
