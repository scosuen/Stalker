package com.scott.stalker.bll;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scott.stalker.dao.UserDao;
import com.scott.stalker.model.User;
import com.scott.stalker.utils.GUIDUtils;

@Component
public class UserBll {
	
	@Autowired
	private UserDao userDao;

	public User login (User user) {
		user.setAccessToken(GUIDUtils.generateNewGUID());
		try {
			return userDao.login(user);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * require userName, accessToken
	 * @param user
	 * @return
	 */
	public User validate(User user) {
		return userDao.validate(user);
	}

	public User updatePwd(User user) {
		try {
			return userDao.updatePwd (user);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void validateTimeoutUsers (Long msce){
		userDao.validateTimeoutUsers (msce);
	}

	public User logout(User user) {
		
		return userDao.logout(user);
	}
}
