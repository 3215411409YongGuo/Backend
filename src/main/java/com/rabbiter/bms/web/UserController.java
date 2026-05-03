package com.rabbiter.bms.web;

import com.rabbiter.bms.model.User;
import com.rabbiter.bms.service.UserService;
import com.rabbiter.bms.utils.MyResult;
import com.rabbiter.bms.utils.MyUtils;
import com.rabbiter.bms.utils.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    // Log in
    @RequestMapping(value = "/login")
    public Map<String, Object> login(@RequestBody User user) {
        // Log in
        User userObj = userService.login(user);
        if(userObj == null) {   // Incorrect account or password
            // Return the result object
            return MyResult.getResultMap(420, "Incorrect account or password");
        } else {    // Account and password are correct.
            // Create token
            String token = TokenProcessor.getInstance().makeToken();
            // Save to Redis
            userService.saveUser(token, userObj);
            // Return the result object
            return MyResult.getResultMap(200, "Login successful",
                    new HashMap<String, String>(){{ put("token", token); }});
        }
    }

    // View user information
    @RequestMapping(value = "/info")
    public Map<String, Object> info(String token) {
        // Retrieve the user from Redis
        User user = userService.getUser(token);
        if(user == null) {  // Failed to obtain
            return MyResult.getResultMap(420, "Failed to obtain user information");
        } else {    // Achieve success
            return MyResult.getResultMap(200, "Successfully obtained user information", user);
        }
    }

    // Log out
    @RequestMapping(value = "/logout")
    public Map<String, Object> logout(String token) {
        // Remove the user from Redis
        userService.removeUser(token);
        return MyResult.getResultMap(200, "Logout successful" );
    }

    // Registration
    @RequestMapping(value = "/register")
    public Integer register(String username, String password){
        return userService.register(username, password);
    }

    // Change password
    @RequestMapping(value = {"/alterPassword", "reader/alterPassword"})
    public Integer alterPassword(Integer userid, String username, Byte isadmin, String oldPassword, String newPassword){
        //Check if the old password is correct
        User userObj = new User();
        userObj.setUserid(userid);
        userObj.setUsername(username);
        userObj.setUserpassword(oldPassword);
        userObj.setIsadmin(isadmin);

        User user = userService.login(userObj);
        if(user == null) {  //Incorrect old password
            return 0;
        } else {    //Old password is correct. Set a new password.
            userService.setPassword(userObj.getUserid(), newPassword);
            return 1;
        }
    }

    // The quantity obtained
    @GetMapping(value = "/getCount")
    public Integer getCount(){
        return userService.getCount();
    }

    // Query all users
    @GetMapping(value = "/queryUsers")
    public List<User> queryUsers(){
        return userService.queryUsers();
    }

    // Perform page-based query on users params: {page, limit, username}
    @GetMapping(value = "/queryUsersByPage")
    public Map<String, Object> queryUsersByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        int count = userService.getSearchCount(params);
        List<User> users = userService.searchUsersByPage(params);
        return MyResult.getListResultMap(0, "success", count, users);
    }

    // Add user
    @PostMapping(value = "/addUser")
    public Integer addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    // Delete user
    @DeleteMapping(value = "/deleteUser")
    public Integer deleteUser(@RequestBody User user){
        return userService.deleteUser(user);
    }

    // Delete some users
    @DeleteMapping(value = "/deleteUsers")
    public Integer deleteUsers(@RequestBody List<User> users){
        return userService.deleteUsers(users);
    }

    // Update user
    @RequestMapping(value = "/updateUser")
    public Integer updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }
}
