package com.revature;

import com.revature.controllers.AuthController;
import com.revature.models.User;
import com.revature.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.websocket.servlet.UndertowWebSocketServletWebServerCustomizer;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
class ECommerceApplicationTests {

	private List<User> testUsers = new ArrayList<User>();
	@Autowired
	private AuthService authService;

	@Autowired
	private AuthController authController;



}
