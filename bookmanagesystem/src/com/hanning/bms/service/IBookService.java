package com.hanning.bms.service;

import com.hanning.bms.entity.Book;

import java.util.List;

public interface IBookService {
    int insertBook(Book book);
    int deleteBook(int id);
    int updateBook(Book book);
    Book selectBookById(int id);
    List<Book> selectBookByName(String bookName);
    List<Book> selectAllBooks();
}
