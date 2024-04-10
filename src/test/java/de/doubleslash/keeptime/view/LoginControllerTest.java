package de.doubleslash.keeptime.view;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {
  LoginController loginController;

   @Test
   public void testExtractValue() {
      String inputUser = "${BASIC_AUTH_USER:user}";
      String inputPassword = "${BASIC_AUTH_PASSWORD:123}";
      String expectedUser = "user";
      String expectedPassword = "123";
      String resultUser = LoginController.extractValue(inputUser);
      String resultPassword = LoginController.extractValue(inputPassword);
      assertEquals(expectedUser, resultUser);
      assertEquals(expectedPassword,resultPassword);
   }

   @Test
   public void testExtractValuePassword() {
      String inputPassword = "${BASIC_AUTH_PASSWORD:123}";
      String expectedPassword = "123";
      String resultPassword = LoginController.extractValue(inputPassword);
      assertEquals(expectedPassword,resultPassword);
   }

   @Test
   public void testCreateAndSaveUser() {
      String username = "testUser";
      String password = "testPassword";
      LoginController loginController = new LoginController(username, password);

      loginController.createAndSaveUser();
      Properties properties = loginController.properties;
      assertEquals("${BASIC_AUTH_USER:testUser}", properties.getProperty("spring.security.user.name"));
      assertEquals("${BASIC_AUTH_PASSWORD:testPassword}", properties.getProperty("spring.security.user.password"));
   }
}

