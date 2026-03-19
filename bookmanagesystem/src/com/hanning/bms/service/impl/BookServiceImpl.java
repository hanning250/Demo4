package com.hanning.bms.service.impl;

import com.hanning.bms.dao.IBookDao;
import com.hanning.bms.dao.impl.BookDaoImpl;
import com.hanning.bms.entity.Book;
import com.hanning.bms.service.IBookService;

import java.util.List;

public class BookServiceImpl implements IBookService {
private IBookDao bookDao = new BookDaoImpl();
    @Override
    public int insertBook(Book book) {
        return bookDao.insertBook(book);
    }

    @Override
    public int deleteBook(int id) {
        return bookDao.deleteBook(id);
    }

    @Override
    public int updateBook(Book book) {
        return bookDao.updateBook(book);
    }

    @Override
    public Book selectBookById(int id) {
        return bookDao.selectBookById(id);
    }

    @Override
    public List<Book> selectBookByName(String bookName) {
        return bookDao.selectBookByName(bookName);
    }

    @Override
    public List<Book> selectAllBooks() {
        return bookDao.selectAllBooks();
    }
}
