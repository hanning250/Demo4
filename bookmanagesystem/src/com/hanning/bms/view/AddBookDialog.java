package com.hanning.bms.view;

import com.hanning.bms.entity.Book;
import com.hanning.bms.service.IBookService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddBookDialog extends JDialog {
    private IBookService bookService;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField categoryField;
    private JTextField publisherField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton saveButton;
    private JButton cancelButton;

    public AddBookDialog(Frame owner, IBookService bookService) {
        super(owner, "添加图书", true);
        this.bookService = bookService;
        
        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        categoryField = new JTextField(20);
        publisherField = new JTextField(20);
        priceField = new JTextField(20);
        stockField = new JTextField(20);
        
        saveButton = new JButton("保存");
        cancelButton = new JButton("取消");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saveBook();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void layoutComponents() {
        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 书名
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("书名:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(titleField, gbc);

        // 作者
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("作者:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(authorField, gbc);

        // 分类
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("分类:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(categoryField, gbc);

        // 出版社
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("出版社:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(publisherField, gbc);

        // 价格
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("价格:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(priceField, gbc);

        // 库存
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("库存:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(stockField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入书名", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入作者", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (categoryField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入分类", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (publisherField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入出版社", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (priceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入价格", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (stockField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入库存", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "价格必须大于0", "输入错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的价格", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int stock = Integer.parseInt(stockField.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "库存不能为负数", "输入错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的库存数量", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveBook() {
        Book book = new Book();
        book.setTitle(titleField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setCategory(categoryField.getText().trim());
        book.setPublisher(publisherField.getText().trim());
        book.setPrice(Double.parseDouble(priceField.getText().trim()));
        book.setStock(Integer.parseInt(stockField.getText().trim()));

        int result = bookService.insertBook(book);
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "图书添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "图书添加失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        publisherField.setText("");
        priceField.setText("");
        stockField.setText("");
    }
}