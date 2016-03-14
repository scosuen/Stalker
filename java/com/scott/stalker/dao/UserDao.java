package com.scott.stalker.dao;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.scott.stalker.model.User;
import com.scott.stalker.utils.ObjectConversion;

@Repository
public class UserDao {
	
	private static Logger logger = LoggerFactory.getLogger(UserDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public User login(User user) throws NoSuchAlgorithmException {
		StringBuffer sql = new StringBuffer();
		sql.append(" match (u:user {pwd:'" + user.getEncrypedPwd() + "'}) ");
		sql.append(" where lower(u.userName) = '" + user.getLowercaseUserName() + "' ");
		sql.append(" SET u.lastActivityTime = timestamp(), u.accessToken = '" + user.getAccessToken() + "'");
		sql.append(" return u.userName as userName, u.accessToken as accessToken, u.lastActivityTime as lastActivityTime, u.companyName as companyName ");
		
		logger.debug("login sql:" + sql);
		List<User> userList = jdbcTemplate.query(sql.toString(), new UserMapper());
		return userList == null || userList.size() <= 0 ? null : userList.get(0);
	}

	public User validate(User user) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" match (u:user {accessToken:'" + user.getAccessToken() + "'}) ");
		sql.append(" where lower(u.userName) = '" + user.getLowercaseUserName() + "' ");
		sql.append(" SET u.lastActivityTime = timestamp() ");
		sql.append(" return u.userName as userName, u.accessToken as accessToken, u.lastActivityTime as lastActivityTime, u.companyName as companyName ");
		
		logger.debug("validate sql:" + sql);
		List<User> userList = jdbcTemplate.query(sql.toString(), new UserMapper());
		return userList == null || userList.size() <= 0 ? null : userList.get(0);
	}
	
	public User updatePwd(User user) throws NoSuchAlgorithmException {
		StringBuffer sql = new StringBuffer("");
		sql.append(" match (u:user) ");
		sql.append(" where lower(u.userName) = '" + user.getLowercaseUserName() + "' ");
		sql.append(" set u.pwd = '" + user.getEncrypedPwd() + "' ");
		sql.append(" return u.userName as userName, u.accessToken as accessToken, u.lastActivityTime as lastActivityTime, u.companyName as companyName ");
		
		logger.debug("updatePwd sql:" + sql);
		List<User> userList = jdbcTemplate.query(sql.toString(), new UserMapper());
		return userList == null || userList.size() <= 0 ? null : userList.get(0);
	}
	
	public void validateTimeoutUsers(Long msce) {
		StringBuffer sql = new StringBuffer("");
		long limitTime = new Date().getTime() - msce;
		sql.append("match (u:user) where u.lastActivityTime < " + limitTime + " set u.accessToken = '' return u");
		
		logger.debug("validateTimeoutUsers sql:" + sql);
		jdbcTemplate.update(sql.toString());
	}
	

	public User logout(User user) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" match (u:user) where lower(u.userName) = '" + user.getLowercaseUserName() + "' set u.lastActivityTime = timestamp(), u.accessToken = '' ");
		sql.append(" return u.userName as userName, u.accessToken as accessToken, u.lastActivityTime as lastActivityTime, u.companyName as companyName ");
		logger.debug("logout sql:" + sql);
		
		List<User> userList = jdbcTemplate.query(sql.toString(), new UserMapper());
		return userList == null || userList.size() <= 0 ? null : userList.get(0);
	}
	
	/**
	 * 
	 * @author Ying
	 *
	 */
	private class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserName(rs.getString("userName"));
			user.setAccessToken(rs.getString("accessToken"));
			user.setLastActivityTime(ObjectConversion.long2Date(rs.getLong("lastActivityTime")));
			user.setCompanyName(rs.getString("companyName"));

			return user;
		}

	}


}
