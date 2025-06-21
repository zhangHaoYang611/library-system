package com.example.library.controller;

import com.example.library.entity.BorrowRecord;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.service.BorrowService;
import com.example.library.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<BorrowRecord> records = borrowService.getUserRecords(user.getId());
        model.addAttribute("records", records);
        return "borrow_list";
    }

    @GetMapping("/borrow/{bookId}")
    public String borrow(@PathVariable Integer bookId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Book book = bookService.findById(bookId);
        boolean success = borrowService.borrowBook(user, book);
        model.addAttribute("msg", success ? "借阅成功" : "库存不足");
        return "redirect:/book/list";
    }

    @GetMapping("/return/{recordId}")
    public String returnBook(@PathVariable Integer recordId) {
        BorrowRecord record = borrowService.getRecordById(recordId);
        if (record != null) {
            borrowService.returnBook(record);
        }
        return "redirect:/borrow/list";
    }
}