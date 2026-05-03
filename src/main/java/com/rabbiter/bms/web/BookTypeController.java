package com.rabbiter.bms.web;

import com.rabbiter.bms.model.BookType;
import com.rabbiter.bms.service.BookTypeService;
import com.rabbiter.bms.utils.MyResult;
import com.rabbiter.bms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/bookType")
public class BookTypeController {

    @Autowired
    BookTypeService bookTypeService;

    // The quantity obtained
    @GetMapping(value = "/getCount")
    public Integer getCount(){
        return bookTypeService.getCount();
    }

    // Query all types
    @GetMapping(value = {"/queryBookTypes", "/reader/queryBookTypes"})
    public List<BookType> queryBookTypes(){
        return bookTypeService.queryBookTypes();
    }

    // Paged query for book types params: {page, limit, booktypename}
    @GetMapping(value = "/queryBookTypesByPage")
    public Map<String, Object> queryBookTypesByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        int count = bookTypeService.getSearchCount(params);
        List<BookType> bookTypes = bookTypeService.searchBookTypesByPage(params);
        return MyResult.getListResultMap(0, "success", count, bookTypes);
    }

    // Add Type
    @PostMapping(value = "/addBookType")
    public Integer addBookType(@RequestBody BookType bookType){
        return bookTypeService.addBookType(bookType);
    }

    // Deletion type
    @DeleteMapping(value = "/deleteBookType")
    public Integer deleteBookType(@RequestBody BookType bookType){
        return bookTypeService.deleteBookType(bookType);
    }

    // Delete some types
    @DeleteMapping(value = "/deleteBookTypes")
    public Integer deleteBookTypes(@RequestBody List<BookType> bookTypes){
        return bookTypeService.deleteBookTypes(bookTypes);
    }

    // Update Type
    @PutMapping(value = "/updateBookType")
    public Integer updateBookType(@RequestBody BookType bookType){
        return bookTypeService.updateBookType(bookType);
    }
}
