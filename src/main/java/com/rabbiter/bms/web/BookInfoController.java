package com.rabbiter.bms.web;

import com.rabbiter.bms.service.BookInfoService;
import com.rabbiter.bms.utils.MyResult;
import com.rabbiter.bms.utils.MyUtils;
import com.rabbiter.bms.model.BookInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/bookInfo")
public class BookInfoController {

    @Autowired
    BookInfoService bookInfoService;

    // Obtain the number of books
    @GetMapping(value = "/getCount")
    public Integer getCount(){
        return bookInfoService.getCount();
    }

    // Query all book information
    @GetMapping(value = "/queryBookInfos")
    public List<BookInfo> queryBookInfos(){
        return bookInfoService.queryBookInfos();
    }

    // Page-based search for querying book information: params: {page, limit, bookname, bookauthor, booktypeid}
    @GetMapping(value = "/queryBookInfosByPage")
    public Map<String, Object> queryBookInfosByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        int count = bookInfoService.getSearchCount(params);  // Total obtained
        List<BookInfo> bookInfos = bookInfoService.searchBookInfosByPage(params);  // Page-based query
        return MyResult.getListResultMap(0, "success", count, bookInfos);
    }

    // Add book information
    @PostMapping(value = "/addBookInfo")
    public Integer addBookInfo(@RequestBody BookInfo bookInfo){
        return bookInfoService.addBookInfo(bookInfo);
    }

    // Delete book information
    @DeleteMapping(value = "/deleteBookInfo")
    public Integer deleteBookInfo(@RequestBody BookInfo bookInfo){
        return bookInfoService.deleteBookInfo(bookInfo);
    }

    // Delete some book information
    @DeleteMapping(value = "/deleteBookInfos")
    public Integer deleteBookInfos(@RequestBody List<BookInfo> bookInfos){
        return bookInfoService.deleteBookInfos(bookInfos);
    }

    // Update book information
    @PutMapping(value = "/updateBookInfo")
    public Integer updateBookInfo(@RequestBody BookInfo bookInfo){
        return bookInfoService.updateBookInfo(bookInfo);
    }
}
