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

	public void usersForTest(){
		testUsers.add(new User(1,"DrLokin@gmail.com","1234","Daniel","Lanza"));
		testUsers.add(new User(2,"jackeyBoy@gmail.com","0987","Jack","Smith"));
		testUsers.add(new User(3,"theBoogieMan@gmail.com","Wick","John","Doe"));
		testUsers.add(new User(4,"strangeMan@gmail.com","Crooked","David","Hoover"));
		testUsers.add(new User(5,"KillerQueen","BitesDaDust","Yoshikage","Kira"));
	}

	@Test
	void contextLoads() {

	}

	// Tests if the user registration service works
	@Test
	void testUserRegisterService(){
		usersForTest();
		final User testerUser = authService.register(testUsers.get(0));
		Assertions.assertFalse(testerUser.equals(null));

		String email = testerUser.getEmail();
		String password = testerUser.getPassword();

		Assertions.assertTrue(authService.findByCredentials(email,password).get().getId()==testerUser.getId());


	}


	// Tests if multiple users can be registered.
	@Test
	void testMassUserRegister(){
		usersForTest();
		for(User u:testUsers){
			authService.register(u);
		}

		for(User u:testUsers){
			String email = u.getEmail();
			String password = u.getPassword();

			User tester = authService.findByCredentials(email,password).get();
			Assertions.assertTrue(tester.getId()== u.getId());
		}
	}

	//Tests if the change user password method works
	@Test
	void testUserPasswordChange(){
		usersForTest();
		User tester = testUsers.get(0);

		String email = tester.getEmail();
		String oldPassword = tester.getPassword();
		String newPassword = "007";

		authService.register(tester);
		User newTester = authService.testChangePassword(email,oldPassword,newPassword);

		NoSuchElementException thrown = Assertions.assertThrows(NoSuchElementException.class,() ->{
			authService.findByCredentials(email,oldPassword).get();
		});
		Assertions.assertEquals("No value present",thrown.getMessage());

		Assertions.assertDoesNotThrow(() -> {
			authService.findByCredentials(email,newPassword).get();
		});

		Assertions.assertEquals(authService.findByCredentials(email,newPassword).get().getId(),tester.getId());

	}

}
