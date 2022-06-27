package uz.jl.library.controller;

import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Controller
@RequestMapping("/book")
public class BookController {

    private final List<Book> books = new ArrayList<>();
    private AtomicLong counter = new AtomicLong(1);

    {
        books.add(new Book(counter.getAndIncrement(),
                "Daftar xoshiyasidagi bitiklar",
                300,
                "O'tkir Hoshimov"));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model) {
        model.addAttribute("books", books);
        return "book/book_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage() {
        return "book/book_add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute Book book) {
        book.setId(counter.getAndIncrement());
        books.add(book);
        return "redirect:/book";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePage(@PathVariable Long id, Model model) {
        method(id, model);
        return "book/book_delete";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable Long id) {
        books.removeIf(book -> book.getId().equals(id));
        return "redirect:/book";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updatePage(@PathVariable Long id, Model model) {
        method(id, model);
        return "book/book_update";
    }

    private void method(@PathVariable Long id, Model model) {
        boolean success;
        Optional<Book> optionalBook = books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
        success = optionalBook.isPresent();
        model.addAttribute("success", success);
        model.addAttribute("book", optionalBook.orElse(null));
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute Book book) {
        Optional<Book> first = books.stream()
                .filter(book1 -> book1.getId().equals(book.getId()))
                .findFirst();
        if (!book.getName().isEmpty()) first.get().setName(book.getName());
        if (!book.getAuthor().isEmpty()) first.get().setAuthor(book.getAuthor());
        if (book.getPageCount() != null) first.get().setPageCount(book.getPageCount());
        return "redirect:/book";
    }

}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Book {
    private Long id;
    private String name;
    private Integer pageCount;
    private String author;
}
