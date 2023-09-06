package com.example.loanService.service;

import com.example.loanService.dto.LoanDTO;
import com.example.loanService.dto.LoanReturnDTO;
import com.example.loanService.entity.Book;
import com.example.loanService.entity.BookStatus;
import com.example.loanService.entity.LoanRecord;
import com.example.loanService.entity.User;
import com.example.loanService.exception.ResourceNotFoundException;
import com.example.loanService.repository.BookRepository;
import com.example.loanService.repository.LoanRepository;
import com.example.loanService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.loanService.entity.BookStatus.AVAILABLE;
import static com.example.loanService.entity.BookStatus.LOANED;


@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    private BookStatus status;


    public List<LoanRecord> getLoanRecords(Long userId) {
        List<LoanRecord> loanRecords = loanRepository.findByUserId(userId);
        return loanRecords;
    }

    public ResponseEntity<?> getLoanRecord(Long bookId) {
        LoanRecord loanRecord = loanRepository.findByBookId(bookId);
        return new ResponseEntity<>(loanRecord.getDueDate(), HttpStatus.OK);
    }

    public ResponseEntity<?> issue(LoanDTO loanDTO) {
        User user = userRepository.findById(loanDTO.getUserId()).orElse(null);
        List<LoanRecord> loanRecords = loanRepository.findByUserId(user.getId());
        int limit = 4;
        Book book = bookRepository.findById(loanDTO.getBookId()).orElse(null);
        if (loanRecords.size() < limit && AVAILABLE == book.getStatus()) {
            LoanRecord loanRecord = LoanRecord.builder()
                    .userId(user.getId())
                    .bookId(loanDTO.getBookId())
                    .issueDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(14))
                    .build();
            book.setTitle(book.getTitle());
            book.setISBN(book.getISBN());
            book.setStatus(LOANED);
            bookRepository.save(book);
            return ResponseEntity.ok(loanRepository.save(loanRecord));
        }
        return new ResponseEntity<>(new IllegalArgumentException("book is not available for loan or Exc"), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> returnBook(LoanReturnDTO loanReturnDTO) throws ResourceNotFoundException {

        LoanRecord loanRecord = loanRepository.findById(loanReturnDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("LoanRecord Not Found"));
        Book book = bookRepository.findById(loanRecord.getBookId()).orElse(null);
        if (loanRecord != null && LOANED == book.getStatus()) {
            loanRecord.setReturnDate(LocalDate.now());
            book.setStatus(AVAILABLE);
            return new ResponseEntity<>(loanRepository.save(loanRecord), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new ResourceNotFoundException("Loan Record not found"), HttpStatus.UNAUTHORIZED);
    }
}
