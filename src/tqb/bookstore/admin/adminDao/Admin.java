package tqb.bookstore.admin.adminDao;

public class Admin {

	private String adminname;
	private String password;

	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Admin [adminname=" + adminname + ", password=" + password + "]";
	}

}
