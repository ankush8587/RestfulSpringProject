package com.incisiveapis.commons.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.incisiveapis.commons.beans.MessageElements;
import com.incisiveapis.commons.beans.User;
import com.incisiveapis.commons.beans.Users;
import com.incisiveapis.helper.UserDao;

@EnableWebMvc
@Controller
public class UserRegistrationController {
	
	@RequestMapping(value = "/Registration", method = RequestMethod.GET)
	public @ResponseBody Users getAllRegisteredUsers() {
		UserDao userDao = new UserDao();
		Users users = new Users();
		List<User> userList = userDao.getAllUsers();
		MessageElements msg = new MessageElements();
		if(userList==null || userList.size()==0){
			msg.setErrorCode("200");
			msg.setErrorMessage("No registered user found");
			
		}else{
		msg.setErrorCode("200");
		msg.setErrorMessage("Success");
		}
		users.setUsers(userList);
		users.setMessage(msg);
		return users;

	}

	@RequestMapping(value = "/Registration", method = RequestMethod.POST)
	public @ResponseBody Users registerUser(
			@RequestParam("userName") String name,
			@RequestParam("email") String email) {
		UserDao userDao = new UserDao();
		Users users = new Users();
		List<User> userList = new ArrayList<User>();
		MessageElements me = new MessageElements();
		User user = new User();
		if (name != null && name != "" && email != null && email != "") {

			user.setEmail(email);
			user.setUserName(name);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			user.setDate(dateFormat.format(date));
			if(userDao.addUser(user)){
			me.setErrorMessage("Success");
			me.setErrorCode("200");
			users.setMessage(me);
			userList.add(user);
			users.setUsers(userList);
			}
		} else {
			me.setErrorMessage("Failed to Register");
			me.setErrorCode("400");
			users.setMessage(me);

		}
		return users;
	}

	@RequestMapping(value = "/Registration", method = RequestMethod.DELETE)
	public @ResponseBody MessageElements deleteUser(@RequestParam("email") String email) {
		UserDao userDao = new UserDao();
		MessageElements msg = new MessageElements();
		if (email != null && email != "") {
			User user = new User();
			user.setEmail(email);
            if(userDao.deleteUser(user)){
            	msg.setErrorCode("200");
    			msg.setErrorMessage("Success");
            }
            else {
    			msg.setErrorCode("400");
    			msg.setErrorMessage("Failed to delete user");
            }
		} else {
			msg.setErrorCode("400");
			msg.setErrorMessage("Failed to delete user");
		}
		return msg;
	}
}
