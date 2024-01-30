package com.twitter.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.twitter.bean.Admin;
import com.twitter.bean.User;
import com.twitter.services.AdminService;
import com.twitter.services.AdminServiceImpl;


/**
 * Servlet implementation class ServerLoginController
 */
@WebServlet("/AdminLoginController")
public class AdminLoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminLoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = request.getParameter("email");
		String password = request.getParameter("password");		
		
		AdminService adminService = new AdminServiceImpl();
		Admin admin = new Admin();
		//ArrayList<User> listUser = new ArrayList<User>();
		//User user = new User();
		admin = adminService.selectAdmin(email, password);
		if((admin.getEmail()!=null && admin.getEmail()!="") && (admin.getPassword()!=null && admin.getPassword()!="")){
			
			HttpSession session = request.getSession();			
			session.setAttribute("emailMsg", admin.getEmail());
					
			RequestDispatcher rd = request.getRequestDispatcher("admin_home.jsp");
			rd.forward(request, response);
		}else{
			request.setAttribute("ErrMsg", "Invalid User!...");
			RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
			rd.include(request, response);

		}
	}

}
