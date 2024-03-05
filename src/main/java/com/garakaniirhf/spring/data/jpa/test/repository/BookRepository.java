package com.garakaniirhf.spring.data.jpa.test.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.garakaniirhf.spring.data.jpa.test.model.Book;
       

public interface BookRepository extends JpaRepository<Book, Long> {
  List<Book> findByRated(boolean rated);

  List<Book> findByCaptionContaining(String caption);
}
