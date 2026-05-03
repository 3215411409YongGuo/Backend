package com.rabbiter.bms.web;

import com.rabbiter.bms.exception.NotEnoughException;
import com.rabbiter.bms.exception.OperationFailureException;
import com.rabbiter.bms.model.Borrow;
import com.rabbiter.bms.service.BookInfoService;
import com.rabbiter.bms.service.BorrowService;
import com.rabbiter.bms.utils.MyResult;
import com.rabbiter.bms.utils.MyUtils;
import com.rabbiter.bms.model.BookInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/borrow")
public class BorrowController {

    @Autowired
    BorrowService borrowService;
    @Autowired
    BookInfoService bookInfoService;

    // Paged query for borrowing: params: {page, limit, userid, bookid}
    @RequestMapping(value = "/queryBorrowsByPage")
    public Map<String, Object> queryBorrowsByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        int count = borrowService.getSearchCount(params);
        List<Borrow> borrows = borrowService.searchBorrowsByPage(params);
        return MyResult.getListResultMap(0, "success", count, borrows);
    }

    // Add borrowing

    @RequestMapping(value = "/addBorrow")
    public Integer addBorrow(@RequestBody Borrow borrow){
        return borrowService.addBorrow(borrow);
    }

    // The quantity obtained
    @RequestMapping(value = "/getCount")
    public Integer getCount(){
        return borrowService.getCount();
    }

    // Delete borrowing
    @RequestMapping(value = "/deleteBorrow")
    public Integer deleteBorrow(@RequestBody Borrow borrow){
        return borrowService.deleteBorrow(borrow);
    }

    // Delete some borrowings
    @RequestMapping(value = "/deleteBorrows")
    public Integer deleteBorrows(@RequestBody List<Borrow> borrows){
        return borrowService.deleteBorrows(borrows);
    }

    // Update Borrowing
    @RequestMapping(value = "/updateBorrow")
    public Integer updateBorrow(@RequestBody Borrow borrow){
        return borrowService.updateBorrow(borrow);
    }

    // Borrowing books
    @RequestMapping(value = {"/borrowBook", "/reader/borrowBook"})
    @Transactional
    public Integer borrowBook(Integer userid, Integer bookid){
        try{
            // Inquire about the details of this book
            BookInfo theBook = bookInfoService.queryBookInfoById(bookid);

            if(theBook == null) {  // The book does not exist.
                throw new NullPointerException("Books" + bookid + "does not exist");
            } else if(theBook.getIsborrowed() == 1) {  // It has already been borrowed.
                throw new NotEnoughException("Books" + bookid + "insufficient inventory (already borrowed)");
            }

            // Update the "isBorrowed" column in the book table
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookid(bookid);
            bookInfo.setIsborrowed((byte) 1);
            Integer res2 = bookInfoService.updateBookInfo(bookInfo);
            if(res2 == 0) throw new OperationFailureException("Books" + bookid + "failed to update the borrowed information");

            // Add a record to the "borrow" table
            Borrow borrow = new Borrow();
            borrow.setUserid(userid);
            borrow.setBookid(bookid);
            borrow.setBorrowtime(new Date(System.currentTimeMillis()));
            Integer res1 = borrowService.addBorrow2(borrow);
            if(res1 == 0) throw new OperationFailureException("Books" + bookid + "failed to add borrowing record");

        } catch (Exception e) {
            System.out.println("An abnormal situation has occurred. Perform a manual rollback.");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    // Return the books
    @RequestMapping(value = {"/returnBook", "/reader/returnBook"})
    @Transactional
    public Integer returnBook(Integer borrowid, Integer bookid){
        try {
            // Inquire about the details of this book
            BookInfo theBook = bookInfoService.queryBookInfoById(bookid);
            // Check the borrowing status of books
            Borrow theBorrow = borrowService.queryBorrowsById(borrowid);

            if(theBook == null) {  // The book does not exist.
                throw new NullPointerException("Books" + bookid + "does not exist");
            } else if(theBorrow == null) {   //No end record exists.
                throw new NullPointerException("The borrowing record " + bookid + "does not exist");
            } else if(theBorrow.getReturntime() != null) {  // The book has already been returned
                throw new NotEnoughException("Books" + bookid + "has been returned");
            }

            // Update the "isBorrowed" column in the book table
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookid(bookid);
            bookInfo.setIsborrowed((byte) 0);
            Integer res2 = bookInfoService.updateBookInfo(bookInfo);
            if(res2 == 0) throw new OperationFailureException("Books" + bookid + "failed to update borrowed information");

            // Update the "Borrow" table and update the end time.
            Borrow borrow = new Borrow();
            borrow.setBorrowid(borrowid);
            borrow.setReturntime(new Date(System.currentTimeMillis()));
            Integer res1 = borrowService.updateBorrow2(borrow);
            if(res1 == 0) throw new OperationFailureException("Books" + bookid + "failed to update borrowed information");

        } catch (Exception e) {
            System.out.println("An abnormal situation has occurred. Perform a manual rollback");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

}
