package tqb.bookstore.user.service;

import java.util.List;

import tqb.bookstore.user.dao.UserDao;
import tqb.bookstore.user.domain.User;

public class UserService {
	private UserDao userDao = new UserDao();

	// 用户注册功能
	public void regist(User form) throws UserException {
		// User user = new User();

		// 如果用户名不为空，则存在
		if (userDao.findUserByName(form.getUsername()) != null) {
			throw new UserException("用户名已被注册");
		}

		// 如果邮箱不为空，则存在
		if (userDao.findUserByEmail(form.getEmail()) != null) {
			throw new UserException("邮箱已被注册");
		}

		// 向数据库中添加用户信息
		userDao.add(form);
	}

	// 用户激活账户功能
	public void active(String code) throws UserException {
		User user = userDao.findUserByCode(code);

		// 通过激活码未查询到账户
		if (user == null) {
			throw new UserException("激活码无效！");
		}

		// 账户已激活
		if (user.isState()) {
			throw new UserException("账户已激活，切勿重复激活！");
		}

		// 激活账户
		userDao.updateCode(user.getUid(), true);

	}

	// 用户登录功能
	public User login(User form) throws UserException {
		User user = userDao.findUserByName(form.getUsername());

		if (user == null) {
			throw new UserException("该用户不存在！");
		}

		if (user.isState() == false) {
			throw new UserException("该账户未激活，请立即登录邮箱进行激活！");
		}

		if (!user.getPassword().equals(form.getPassword())) {
			throw new UserException("密码输入错误！");
		}

		return user;
	}

	public List<User> findAll() {
		return userDao.findAll();
	}

	public User editPre(String uid) {
		return userDao.findUserByUid(uid);
	}

	public void edit(User user) throws UserException {
		User _user = userDao.findUserByUid(user.getUid());
		if (user.getPassword().equals(_user.getPassword())){
			throw new UserException("修改的密码不能与之前的密码相同！");
		}
		userDao.updatePassword(user);
	}

	public void delete(String uid) {
		userDao.delete(uid);
	}
}
