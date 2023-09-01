package com.example.loanService.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="loan_record")
public class LoanRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(name ="user_id")
    private Long userId;
    @JoinColumn(name = "book_id")
    private Long bookId;
    private LocalDate issueDate;
    private LocalDate dueDate;

    private  LocalDate returnDate;

}
