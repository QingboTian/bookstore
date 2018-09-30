package tqb.bookstore.user.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.jdbc.TxQueryRunner;
import tqb.bookstore.user.domain.User;

public class UserDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 通过用户名查询
	 * 
	 * @param username
	 * @return
	 */
	public User findUserByName(String username) {
		try {
			String sql = "select *from user where username=?";
			User user = qr.query(sql, new BeanHandler<User>(User.class), username);
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过邮箱查询
	 * 
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email) {
		try {
			String sql = "select *from user where email=?";
			User user = qr.query(sql, new BeanHandler<User>(User.class), email);
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 注册成功，添加用户
	 * 
	 * @param user
	 */
	public void add(User user) {
		try {
			String sql = "insert into user values(?,?,?,?,?,?)";
			Object[] params = { user.getUid(), user.getUsername(), user.getPassword(), user.getEmail(), user.getCode(),
					user.isState() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过激活码查询
	 * 
	 * @param username
	 * @return
	 */
	public User findUserByCode(String code) {
		try {
			String sql = "select *from user where code=?";
			User user = qr.query(sql, new BeanHandler<User>(User.class), code);
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 更新激活状态
	 * 
	 * @param uid
	 * @param state
	 */
	public void updateCode(String uid, boolean state) {
		try {
			String sql = "update user set state=? where uid=?";
			qr.update(sql, state, uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<User> findAll() {
		try {
			String sql = "select * from user";
			List<User> userList = qr.query(sql, new BeanListHandler<User>(User.class));
			return userList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public User findUserByUid(String uid) {
		try {
			String sql = "select *from user where uid=?";
			User user = qr.query(sql, new BeanHandler<User>(User.class), uid);
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void updatePassword(User user) {
		try {
			String sql = "update user set password=? where uid=?";
			qr.update(sql, user.getPassword(), user.getUid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void delete(String uid) {
		try {
			String sql = "delete from user where uid=?";
			qr.update(sql, uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
