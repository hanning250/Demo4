package com.hanning.bms.dao.impl;

import com.hanning.bms.dao.IBookDao;
import com.hanning.bms.entity.Book;
import com.hanning.bms.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class BookDaoImpl implements IBookDao {
    private QueryRunner qr = new QueryRunner();
    @Override
    public int insertBook(Book book) {
        String sql = "insert into tb_book(title,author,category,publisher,price,stock) values(?,?,?,?,?,?)";
        try {
            return qr.update(DBUtil.getConn(), sql,
                    book.getTitle(), book.getAuthor(), book.getCategory(),
                    book.getPublisher(), book.getPrice(), book.getStock());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteBook(int id) {
        String sql = "delete from tb_book where id = ?";
        try {
            return qr.update(DBUtil.getConn(), sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateBook(Book book) {
        String sql = "update tb_book set title=?,author=?,category=?,publisher=?,price=?,stock=? where id = ?";
        try {
            return qr.update(DBUtil.getConn(), sql, book.getTitle(), book.getAuthor(), book.getCategory(),
                    book.getPublisher(), book.getPrice(), book.getStock(), book.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Book selectBookById(int id) {
        String sql = "select * from tb_book where id = ?";
        try {
            return qr.query(DBUtil.getConn(), sql, new BeanHandler<>(Book.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> selectBookByName(String bookName) {
        String sql = "select * from tb_book where title like ?";
        try {
            return qr.query(DBUtil.getConn(), sql, new BeanListHandler<>(Book.class), "%" + bookName + "%");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> selectAllBooks() {
        String sql = "select * from tb_book";
        try {
            return qr.query(DBUtil.getConn(), sql, new BeanListHandler<>(Book.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
