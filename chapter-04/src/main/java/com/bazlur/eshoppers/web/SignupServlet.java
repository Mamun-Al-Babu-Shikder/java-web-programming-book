package com.bazlur.eshoppers.web;

import com.bazlur.eshoppers.dto.UserDTO;
import com.bazlur.eshoppers.repository.UserRepositoryImpl;
import com.bazlur.eshoppers.service.UserService;
import com.bazlur.eshoppers.service.UserServiceImpl;
import com.bazlur.eshoppers.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
	private final static Logger LOGGER = LoggerFactory.getLogger(SignupServlet.class);

	private UserService userService = new UserServiceImpl(new UserRepositoryImpl());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {
		LOGGER.info("serving singup page");

		req.getRequestDispatcher("/WEB-INF/signup.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {
		var userDTO = copyParametersTo(req);
		var errors = ValidationUtil.getInstance().validate(userDTO);

		if (errors.isEmpty()) {
			LOGGER.info("user is valid, creating a new user with: {}", userDTO);
			userService.saveUser(userDTO);
			resp.sendRedirect("/home");
		} else {
			req.setAttribute("errors", errors);
			LOGGER.info("User sent invalid data: {}", userDTO);
			req.getRequestDispatcher("/WEB-INF/signup.jsp").forward(req, resp);
		}
	}

	private UserDTO copyParametersTo(HttpServletRequest req) {
		var userDTO = new UserDTO();
		userDTO.setFirstName(req.getParameter("firstName"));
		userDTO.setLastName(req.getParameter("lastName"));
		userDTO.setPassword(req.getParameter("password"));
		userDTO.setPasswordConfirmed(req.getParameter("passwordConfirmed"));
		userDTO.setEmail(req.getParameter("email"));
		userDTO.setUsername(req.getParameter("username"));

		return userDTO;
	}
}

