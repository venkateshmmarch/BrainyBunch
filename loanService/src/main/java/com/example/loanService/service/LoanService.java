package com.example.loanService.service;

import com.example.loanService.dto.LoanDTO;
import com.example.loanService.dto.LoanReturnDTO;
import com.example.loanService.entity.Book;
import com.example.loanService.entity.BookStatus;
import com.example.loanService.entity.LoanRecord;
import com.example.loanService.entity.User;
import com.example.loanService.exception.ResourceNotFoundException;
import com.example.loanService.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.loanService.entity.BookStatus.AVAILABLE;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    private Book book;
    private User user;
    public List<LoanRecord> getLoanRecords(Long userId) {
        List<LoanRecord> loanRecords = loanRepository.findByUserId(userId);
        return loanRecords;
    }

    public ResponseEntity<?> getLoanRecord(Long bookId) {
        LoanRecord loanRecord = loanRepository.findByBookId(bookId);
        return new ResponseEntity<>(loanRecord.getDueDate(), HttpStatus.OK);
    }

    public ResponseEntity<LoanRecord> issue(LoanDTO loanDTO) {
        List<LoanRecord> loanRecords = loanRepository.findByUserId(loanDTO.getUserId());
        int limit = 4;

        if (loanRecords.size() < limit && book.getStatus()==AVAILABLE) {
            LoanRecord loanRecord = LoanRecord.builder()
                    .userId(loanDTO.getUserId())
                    .bookId(loanDTO.getBookId())
                    .issueDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(14))
                    .build();
            Book updateStatus=Book.builder().status(BookStatus.LOANED).build();
            return ResponseEntity.ok(loanRepository.save(loanRecord));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public LoanRecord returnBook(LoanReturnDTO loanReturnDTO) throws ResourceNotFoundException {
        LoanRecord loanRecord = loanRepository.findById(loanReturnDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("LoanRecod Not Found"));
        loanRecord.setReturnDate(LocalDate.now());

        return loanRepository.save(loanRecord);
    }
}
