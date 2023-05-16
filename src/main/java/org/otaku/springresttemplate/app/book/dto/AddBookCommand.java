package org.otaku.springresttemplate.app.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddBookCommand {
    @Size(min = 3)
    @NotBlank
    private String name;
    @NotBlank
    private String isbn;
}
