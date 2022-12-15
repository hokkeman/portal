package portal;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginLogoutServlet")
public class LoginLogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginLogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/TopPage.jsp").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String pid = request.getParameter("PID");
		String page = null;

		switch(pid) {
			case "UserLogin": // 一般ユーザーログインボタン
				String userId = request.getParameter("userid");
				String salt = LoginDAO.saltGet(userId);
				String userpass = Hash.hashing(request.getParameter("userpass"), salt); // 入力されたパスワードをハッシュ化

				// ログイン認証
				AccountDataBean loginBean = new AccountDataBean();
				loginBean.setUserid(userId);
				loginBean.setUserpass(userpass);
				AccountDataBean loginUserData = LoginDAO.loginCheck(loginBean);

				if(loginUserData != null) { // ログイン成功
					// ログインユーザーの所属グループデータを取得
					ArrayList<GroupDataBean>affiliationList = GroupDAO.affiliationData(loginUserData);
					session.setAttribute("AffiliationList", affiliationList); // 所属するグループのデータをセッションに保存
					session.setAttribute("LoginUser", loginUserData);	// ログインするユーザーの情報をセッションに保存
					page = "/UserPage.jsp";
				} else { // ログイン失敗
					session.setAttribute("ErrorMsg", "LoginError");
					page = "/Error.jsp";
				}
				break;
			case "AdminLogin": // 管理者ログインボタン
				String adminId = request.getParameter("AdminId");
				String saltAd = LoginDAO.saltAdGet(adminId);
				String adminpass = Hash.hashing(request.getParameter("AdminPass"), saltAd); // 入力されたパスワードをハッシュ化

				AccountDataBean adLoginBean = new AccountDataBean();
				adLoginBean.setUserid(adminId);
				adLoginBean.setUserpass(adminpass);
				AccountDataBean returnAdLogin = LoginDAO.adminloginCheck(adLoginBean);

				if(returnAdLogin != null) {
					session.setAttribute("LoginAdminUser", returnAdLogin);	// セッションにログイン情報を保存
					page = "/AdminPage.jsp";
				} else {
					session.setAttribute("ErrorMsg", "AdminLoginError");
					page = "/Error.jsp";
				}
				break;
			case "Logout":
				session.invalidate(); // セッションを破棄
				page = "/TopPage.jsp";
				break;
				}
		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}

}
