package org.otaku.springresttemplate.app.book.exception;

import org.otaku.springresttemplate.app.rest.ErrorCodes;
import org.otaku.springresttemplate.infrastructure.rest.BusinessException;

public class BookNotFoundException extends BusinessException {
    public BookNotFoundException(Long id) {
        super(ErrorCodes.BOOK_NOT_FOUND, "book [%s] not found".formatted(id));
    }
}
