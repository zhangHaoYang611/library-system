package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "book_list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "book_add";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title, @RequestParam String author, @RequestParam Integer stock) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setStock(stock);
        bookService.save(book);
        return "redirect:/book/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        bookService.deleteById(id);
        return "redirect:/book/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<Book> books = bookService.searchBooks(keyword);
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        return "book_list";
    }
}