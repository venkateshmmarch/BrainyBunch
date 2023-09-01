package com.example.loanService.controller;

import com.example.loanService.dto.BookDTO;
import com.example.loanService.entity.Book;
import com.example.loanService.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookController {
     @Autowired
     private BookService bookService;
    @PostMapping
    public Book createBook(@RequestBody BookDTO bookDTO){
        return  bookService.saveBook(bookDTO);

    }
}
