package com.hanning.bms.view;

import com.hanning.bms.entity.Book;
import com.hanning.bms.service.IBookService;
import com.hanning.bms.service.impl.BookServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        while(running){
            printMainMenu();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    doInsertBook();
                    break;
                case 2:
                    doDeleteBook();
                    break;
                case 3:
                    doUpdateBook();
                    break;
                case 4:
                    doQueryBook();
                    break;
                case 0:
                    running = false;
                    System.out.println("欢迎下次再来");
                    break;

            }
        }
    }
    public  static void printMainMenu(){
        System.out.println( "***欢迎使用图书管理系统***");
        System.out.println( "1.添加图书信息");
        System.out.println( "2.删除图书信息");
        System.out.println( "3.修改图书信息");
        System.out.println( "4.查询图书信息");
        System.out.println( "0.退出系统");
        System.out.println("请选择");
    }
    public static void doInsertBook(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要添加的图书名称");
        String title = scanner.nextLine();
        System.out.println("请输入要添加的图书作者");
        String author = scanner.nextLine();
        System.out.println("请输入要添加的图书分类");
        String category = scanner.nextLine();
        System.out.println("请输入要添加的图书出版社");
        String publisher = scanner.nextLine();
        System.out.println("请输入要添加的图书价格");
        double price = scanner.nextDouble();
        System.out.println("请输入要添加的图书库存");
        int stock = scanner.nextInt();

        Book book = new Book(0,title,author,category,publisher,price,stock );
        IBookService service = new BookServiceImpl();
        int rlt = service.insertBook(book);
        System.out.println(rlt > 0?"添加成功":"添加失败");
    }
    public static void doDeleteBook(){

        System.out.println("执行删除图书的操作");
        System.out.println("请输入要删除图书的id");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        IBookService service = new BookServiceImpl();
        int rlt = service.deleteBook(id);
        System.out.println(rlt >0?"删除成功":"删除失败");
    }
    public static void doUpdateBook()
    {
        System.out.println("执行修改图书的操作");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要修改图书的id");
        int id = scanner.nextInt();
        scanner.nextLine();
        IBookService service = new BookServiceImpl();
        System.out.println("请输入要修改的图书名称");
        String title = scanner.nextLine();
        System.out.println("请输入要修改的图书作者");
        String author = scanner.nextLine();
        System.out.println("请输入要修改的图书分类");
        String category = scanner.nextLine();
        System.out.println("请输入要修改的图书出版社");
        String publisher = scanner.nextLine();
        System.out.println("请输入要修改的图书价格");
        double price = scanner.nextDouble();
        System.out.println("请输入要修改的图书库存");
        int stock = scanner.nextInt();

        Book book = new Book(id,title,author,category,publisher,price,stock);
        int rlt = service.updateBook(book);
        System.out.println(rlt >0?"修改成功":"修改失败");
    }
    public static void doQueryBook(){
        boolean running = true;
        while(running){
            printSubMenu();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    doQueryBookById();
                    break;
                case 2:
                    doQueryBookByName();
                    break;
                case 3:
                    doQueryBookByAll();
                    break;
                case 0:
                    running = false;
                    break;


            }
        }
    }

    public static void printSubMenu() {
        System.out.println("1.根据id查询图书");
        System.out.println("2.根据书名查询图书");
        System.out.println("3.查询所有图书");
        System.out.println("0.返回");
        System.out.println("请选择");
    }
    public static void doQueryBookById(){
        System.out.println("执行根据id查询操作...");
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要查询的图书id：");
        int bookId = sc.nextInt();
        // 清空nextInt()残留的换行符（避免后续潜在输入问题，与修复后的doUpdateBook一致）
        sc.nextLine();

        // 调用Service层方法
        IBookService service=new BookServiceImpl();
        Book book = service.selectBookById(bookId);

        // 打印查询结果
        if (book != null) {
            System.out.println("=====查询到图书信息=====");
            System.out.println("图书id：" + book.getId());
            System.out.println("图书名称：" + book.getTitle());
            System.out.println("图书作者：" + book.getAuthor());
            System.out.println("图书分类：" + book.getCategory());
            System.out.println("图书出版社：" + book.getPublisher());
            System.out.println("图书价格：" + book.getPrice());
            System.out.println("图书库存：" + book.getStock());
            System.out.println("======================");
        } else {
            System.out.println("未查询到id为" + bookId + "的图书！");
        }
    }
    public static void doQueryBookByName(){
        System.out.println("执行根据书名查询操作...");

        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要查询的图书名称（支持模糊查询）：");
        String bookName = sc.nextLine(); // 直接读取书名，无换行符残留问题

        // 调用Service层方法（注意类名拼写：BookServiceImpl，需确保已导入）
        IBookService service= new BookServiceImpl();
        List<Book> bookList = service.selectBookByName(bookName);

        // 打印查询结果
        if (bookList != null && !bookList.isEmpty()) {
            System.out.println("=====查询到" + bookList.size() + "本匹配的图书=====");
            // 遍历图书列表，打印每本图书的信息
            for (Book book : bookList) {
                System.out.println("图书id：" + book.getId());
                System.out.println("图书名称：" + book.getTitle());
                System.out.println("图书作者：" + book.getAuthor());
                System.out.println("图书分类：" + book.getCategory());
                System.out.println("图书出版社：" + book.getPublisher());
                System.out.println("图书价格：" + book.getPrice());
                System.out.println("图书库存：" + book.getStock());
                System.out.println("----------------------");
            }
            System.out.println("======================");
        } else {
            System.out.println("未查询到包含【" + bookName + "】的图书！");
        }
    }
    public static void doQueryBookByAll(){
        System.out.println("执行查询所有操作图书");
        System.out.println("执行查询所有图书...");
        System.out.println("执行查询所有图书的操作...");
        // 调用Service层方法（确保已导入BookServiceImpl）
        IBookService service=new BookServiceImpl();
        List<Book> bookList = service.selectAllBooks();

        // 打印查询结果
        if (bookList != null && !bookList.isEmpty()) {
            System.out.println("=====所有图书信息汇总（共" + bookList.size() + "本）=====");
            // 遍历图书列表，打印每本图书的详细信息
            for (Book book : bookList) {
                System.out.println("图书id：" + book.getId());
                System.out.println("图书名称：" + book.getTitle());
                System.out.println("图书作者：" + book.getAuthor());
                System.out.println("图书分类：" + book.getCategory());
                System.out.println("图书出版社：" + book.getPublisher());
                System.out.println("图书价格：" + book.getPrice());
                System.out.println("图书库存：" + book.getStock());
                System.out.println("----------------------");
            }
            System.out.println("======================");
        } else {
            System.out.println("当前图书库中暂无图书记录！");
        }
    }


}
