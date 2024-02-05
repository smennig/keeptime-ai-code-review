package de.doubleslash.keeptime.view;

import java.util.Properties;

public class LoginController {

   private String username;
   private String password;
   Properties properties = new Properties();

   public LoginController(final String username, final String password) {
      this.username = username;
      this.password = password;
   }

   public void createAndSaveUser() {
      properties.setProperty("spring.security.user.name", "${BASIC_AUTH_USER:" + this.username + "}");
      properties.setProperty("spring.security.user.password", "${BASIC_AUTH_PASSWORD:" + this.password + "}");
   }

   public static String extractValue(String input) {
      int startIndex = input.indexOf(":") + 1;
      int endIndex = input.lastIndexOf("}");
      return input.substring(startIndex, endIndex);
   }
}
