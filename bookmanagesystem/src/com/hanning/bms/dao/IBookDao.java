package com.hanning.bms.dao;

import com.hanning.bms.entity.Book;

import java.util.List;

public interface IBookDao {
    int insertBook(Book book);
    int deleteBook(int id);
    int updateBook(Book book);
    Book selectBookById(int id);
    List<Book> selectBookByName(String bookName);
    List<Book> selectAllBooks();
}
