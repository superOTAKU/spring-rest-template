package org.otaku.springresttemplate.app.book.service;

import org.otaku.springresttemplate.app.book.domain.Book;
import org.otaku.springresttemplate.app.book.dto.AddBookCommand;
import org.otaku.springresttemplate.app.book.dto.AddBookResult;
import org.otaku.springresttemplate.app.book.exception.BookNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {
    private final Map<Long, Book> bookMap = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0L);

    //明确可能抛出的异常
    public Book getBookById(Long id) throws BookNotFoundException {
        var book = bookMap.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    public AddBookResult addBook(AddBookCommand addBookRequest) {
        Book book = new Book();
        BeanUtils.copyProperties(addBookRequest, book);
        book.setId(id.incrementAndGet());
        bookMap.put(book.getId(), book);
        return new AddBookResult(book.getId());
    }

}
