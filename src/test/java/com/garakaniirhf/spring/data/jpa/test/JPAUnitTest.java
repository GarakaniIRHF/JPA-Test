package com.garakaniirhf.spring.data.jpa.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.garakaniirhf.spring.data.jpa.test.model.Book;
import com.garakaniirhf.spring.data.jpa.test.repository.BookRepository;

@DataJpaTest
public class JPAUnitTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  BookRepository repository;

  @Test
  public void should_find_no_books_if_repository_is_empty() {
    Iterable<Book> books = repository.findAll();

    assertThat(books).isEmpty();
  }

  @Test
  public void should_store_a_book() {
    Book book = repository.save(new Book("Tut caption", "Tut desc", true));

    assertThat(book).hasFieldOrPropertyWithValue("caption", "Tut caption");
    assertThat(book).hasFieldOrPropertyWithValue("comment", "Tut desc");
    assertThat(book).hasFieldOrPropertyWithValue("rated", true);
  }

  @Test
  public void should_find_all_books() {
    Book tut1 = new Book("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    Book tut2 = new Book("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    Book tut3 = new Book("Tut#3", "Desc#3", true);
    entityManager.persist(tut3);

    Iterable<Book> books = repository.findAll();

    assertThat(books).hasSize(3).contains(tut1, tut2, tut3);
  }

  @Test
  public void should_find_book_by_id() {
    Book tut1 = new Book("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    Book tut2 = new Book("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    Book foundBook = repository.findById(tut2.getId()).get();

    assertThat(foundBook).isEqualTo(tut2);
  }

  @Test
  public void should_find_rated_books() {
    Book tut1 = new Book("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    Book tut2 = new Book("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    Book tut3 = new Book("Tut#3", "Desc#3", true);
    entityManager.persist(tut3);

    Iterable<Book> books = repository.findByRated(true);

    assertThat(books).hasSize(2).contains(tut1, tut3);
  }

  @Test
  public void should_find_books_by_caption_containing_string() {
    Book tut1 = new Book("Spring Boot Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    Book tut2 = new Book("Java Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    Book tut3 = new Book("Spring Data JPA Tut#3", "Desc#3", true);
    entityManager.persist(tut3);

    Iterable<Book> books = repository.findByCaptionContaining("ring");

    assertThat(books).hasSize(2).contains(tut1, tut3);
  }

  @Test
  public void should_update_book_by_id() {
    Book tut1 = new Book("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    Book tut2 = new Book("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    Book updatedTut = new Book("updated Tut#2", "updated Desc#2", true);

    Book tut = repository.findById(tut2.getId()).get();
    tut.setCaption(updatedTut.getCaption());
    tut.setComment(updatedTut.getComment());
    tut.setRated(updatedTut.isRated());
    repository.save(tut);

    Book checkTut = repository.findById(tut2.getId()).get();
    
    assertThat(checkTut.getId()).isEqualTo(tut2.getId());
    assertThat(checkTut.getCaption()).isEqualTo(updatedTut.getCaption());
    assertThat(checkTut.getComment()).isEqualTo(updatedTut.getComment());
    assertThat(checkTut.isRated()).isEqualTo(updatedTut.isRated());
  }

  @Test
  public void should_delete_book_by_id() {
    Book tut1 = new Book("Tut#1", "Desc#1", true);
    entityManager.persist(tut1);

    Book tut2 = new Book("Tut#2", "Desc#2", false);
    entityManager.persist(tut2);

    Book tut3 = new Book("Tut#3", "Desc#3", true);
    entityManager.persist(tut3);

    repository.deleteById(tut2.getId());

    Iterable<Book> books = repository.findAll();

    assertThat(books).hasSize(2).contains(tut1, tut3);
  }

  @Test
  public void should_delete_all_books() {
    entityManager.persist(new Book("Tut#1", "Desc#1", true));
    entityManager.persist(new Book("Tut#2", "Desc#2", false));

    repository.deleteAll();

    assertThat(repository.findAll()).isEmpty();
  }
}
