package org.otaku.springresttemplate.app.book.controller;

import jakarta.validation.Valid;
import org.otaku.springresttemplate.app.book.domain.Book;
import org.otaku.springresttemplate.app.book.dto.AddBookCommand;
import org.otaku.springresttemplate.app.book.dto.AddBookResult;
import org.otaku.springresttemplate.app.book.exception.BookNotFoundException;
import org.otaku.springresttemplate.app.book.service.BookService;
import org.otaku.springresttemplate.infrastructure.rest.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping("/book")
@RestController
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return service.getBookById(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BookNotFoundException.class)
    public ApiError bookNotFound(BookNotFoundException e) {
        return ApiError.valueOf(e);
    }

    @PostMapping
    public AddBookResult addBook(@RequestBody @Valid AddBookCommand request) {
        return service.addBook(request);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }

}
