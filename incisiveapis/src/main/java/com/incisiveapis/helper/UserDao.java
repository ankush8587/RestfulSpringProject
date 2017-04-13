package com.incisiveapis.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.incisiveapis.commons.beans.User;

public class UserDao {
	private final String FILE_NAME = "users.json";
	private  String getFileName() {
		String PATH = this.getClass().getClassLoader().getResource("")
				.getPath();
		String fullPath = "";
		try {
			fullPath = URLDecoder.decode(PATH, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String pathArr[] = fullPath.split("/WEB-INF/classes/");
		fullPath = pathArr[0];
		return fullPath+"/"+FILE_NAME;
	}

	public  List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		try {
			File file = new File(getFileName());
			if (file.exists()) {
				JSONParser parser = new JSONParser();
				FileReader fileReader = new FileReader(file);
				Object obj = parser.parse(fileReader);
				JSONObject jsonObject = (JSONObject) obj;
				JSONArray userListarray = (JSONArray) jsonObject.get("users");
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> iterator = userListarray.iterator();
				while (iterator.hasNext()) {
					JSONObject objUser = (JSONObject) iterator.next();
					userList.add(jsonObjectToUser(objUser));
				}
				fileReader.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveUserList(List<User> userList) {
		try {
			File file = new File(getFileName());
			// if file doesnt exists, then create it
			if (!file.exists()) {
				boolean status = file.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(file);
			JSONArray jsonArray = new JSONArray();
			ObjectMapper mapper = new ObjectMapper();
			for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
				User user = (User) iterator.next();
				jsonArray.add(userToJsonObject(user));
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("users", jsonArray);
			fop.write(jsonObject.toJSONString().getBytes());
			fop.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  boolean addUser(User pUser) {
		List<User> userList = getAllUsers();
		boolean userExists = false;
		int walk = 0;
		for (User user : userList) {
			if (user.getEmail().equalsIgnoreCase(pUser.getEmail())) {
				userExists = true;
				userList.remove(walk);
				userList.add(pUser);
				break;
			}
			walk++;
		}
		if (!userExists) {
			userList.add(pUser);
			userExists=true;
		}
		saveUserList(userList);
		return userExists;
	}

	private User jsonObjectToUser(JSONObject objUser) {
		User user = new User();
				user.setUserName((String) objUser.get(UserConstant.NAME));
				user.setEmail((String) objUser.get(UserConstant.EMAIL));
				user.setDate((String) objUser.get(UserConstant.DATE_CREATED));
				
		return user;
	}

	@SuppressWarnings("unchecked")
	private JSONObject userToJsonObject(User user) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(UserConstant.NAME, user.getUserName());
		jsonObject.put(UserConstant.EMAIL, user.getEmail());
		jsonObject.put(UserConstant.DATE_CREATED, user.getDate());
		return jsonObject;
	}
	
	public boolean deleteUser(User pUser) {
        List<User> userList = getAllUsers();
        boolean userExists = false;
        int walk = 0;
        for (User user : userList) {
            if (user.getEmail().equalsIgnoreCase(pUser.getEmail())) {
                userList.remove(walk);  
                userExists = true;
                break;
            }
            walk++;
        }
        saveUserList(userList);
        return userExists;
        
    }
}