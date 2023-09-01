package com.example.loanService.dto;

import com.example.loanService.entity.Book;
import com.example.loanService.entity.BookStatus;
import com.example.loanService.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LoanDTO {

    private Long userId;

    private Long bookId;

    /*
    private LocalDate issueDate;
    private LocalDate dueDate;
    */

}
