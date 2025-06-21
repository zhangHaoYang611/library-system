package com.example.library.service;

import com.example.library.entity.BorrowRecord;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BorrowService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private BookRepository bookRepository;

    public boolean borrowBook(User user, Book book) {
        if (book.getStock() > 0) {
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);

            BorrowRecord record = new BorrowRecord();
            record.setUser(user);
            record.setBook(book);
            record.setBorrowDate(new Date());
            record.setStatus("borrowed");
            borrowRecordRepository.save(record);
            return true;
        }
        return false;
    }

    public boolean returnBook(BorrowRecord record) {
        if ("borrowed".equals(record.getStatus())) {
            record.setStatus("returned");
            record.setReturnDate(new Date());
            borrowRecordRepository.save(record);

            Book book = record.getBook();
            book.setStock(book.getStock() + 1);
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    public List<BorrowRecord> getUserRecords(Integer userId) {
        return borrowRecordRepository.findByUserId(userId);
    }

    public BorrowRecord getRecordById(Integer recordId) {
        return borrowRecordRepository.findById(recordId).orElse(null);
    }
}