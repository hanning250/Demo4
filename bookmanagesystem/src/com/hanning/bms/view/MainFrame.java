package com.hanning.bms.view;

import com.hanning.bms.entity.Book;
import com.hanning.bms.service.IBookService;
import com.hanning.bms.service.impl.BookServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainFrame extends JFrame {
    private IBookService bookService = new BookServiceImpl();
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public MainFrame() {
        setTitle("图书管理系统");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        loadAllBooks();
    }

    private void initComponents() {
        // 初始化表格模型
        String[] columnNames = {"ID", "书名", "作者", "分类", "出版社", "价格", "库存"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 禁止单元格编辑
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setReorderingAllowed(false);

        // 初始化查询组件
        searchTypeCombo = new JComboBox<>(new String[]{"根据id查询", "根据书名查询", "查询所有"});
        searchField = new JTextField(15);
        searchButton = new JButton("查询");

        // 初始化操作按钮
        addButton = new JButton("添加图书");
        editButton = new JButton("编辑图书");
        deleteButton = new JButton("删除图书");

        // 添加事件监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddBookDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditBookDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBook();
            }
        });

        // 查询类型改变时的处理
        searchTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) searchTypeCombo.getSelectedItem();
                if ("查询所有".equals(selectedType)) {
                    searchField.setEnabled(false);
                } else {
                    searchField.setEnabled(true);
                }
            }
        });
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // 顶部面板 - 查询区域
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("查询类型:"));
        topPanel.add(searchTypeCombo);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // 将查询面板和按钮面板合并到顶部
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(northPanel, BorderLayout.NORTH);

        // 中间面板 - 表格
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadAllBooks() {
        List<Book> books = bookService.selectAllBooks();
        updateTableData(books);
    }

    private void performSearch() {
        String searchType = (String) searchTypeCombo.getSelectedItem();
        String searchText = searchField.getText().trim();

        if ("根据id查询".equals(searchType)) {
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入要查询的ID", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int id = Integer.parseInt(searchText);
                Book book = bookService.selectBookById(id);
                if (book != null) {
                    updateTableData(java.util.Arrays.asList(book));
                } else {
                    JOptionPane.showMessageDialog(this, "未找到ID为 " + id + " 的图书", "查询结果", JOptionPane.INFORMATION_MESSAGE);
                    clearTable();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入有效的ID", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else if ("根据书名查询".equals(searchType)) {
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入要查询的书名", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<Book> books = bookService.selectBookByName(searchText);
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "未找到书名包含 \"" + searchText + "\" 的图书", "查询结果", JOptionPane.INFORMATION_MESSAGE);
                clearTable();
            } else {
                updateTableData(books);
            }
        } else if ("查询所有".equals(searchType)) {
            loadAllBooks();
        }
    }

    private void updateTableData(List<Book> books) {
        tableModel.setRowCount(0); // 清空表格
        for (Book book : books) {
            Object[] rowData = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPublisher(),
                book.getPrice(),
                book.getStock()
            };
            tableModel.addRow(rowData);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    private void openAddBookDialog() {
        AddBookDialog dialog = new AddBookDialog(this, bookService);
        dialog.setVisible(true);
        // 对话框关闭后刷新表格
        loadAllBooks();
    }

    private void openEditBookDialog() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的图书", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 获取选中行的图书ID
        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        Book book = bookService.selectBookById(bookId);
        
        if (book != null) {
            EditBookDialog dialog = new EditBookDialog(this, bookService, book);
            dialog.setVisible(true);
            // 对话框关闭后刷新表格
            loadAllBooks();
        } else {
            JOptionPane.showMessageDialog(this, "无法获取图书信息", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的图书", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "确定要删除选中的图书吗？",
            "确认删除",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // 获取选中行的图书ID
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            int result = bookService.deleteBook(bookId);
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadAllBooks(); // 刷新表格
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}