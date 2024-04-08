// Copyright 2024 doubleSlash Net Business GmbH
//
// This file is part of KeepTime.
// KeepTime is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.

package de.doubleslash.keeptime.REST_API_Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Properties;

import de.doubleslash.keeptime.view.SettingsController;
import org.junit.jupiter.api.Test;

public class ApiTest {

   @Test
   public void testSaveUserAndAuthorities() {
      String username = "user";
      String password = "1234";
      SettingsController settingsController;


      Properties properties = new Properties();
      properties.put("spring.security.user.name", "${BASIC_AUTH_USER:" + username+ "}");
      properties.setProperty("spring.security.user.password", "${BASIC_AUTH_PASSWORD:" + password + "}");

      assertEquals(properties.getProperty("spring.security.user.password"),"${BASIC_AUTH_PASSWORD:1234}");
      assertEquals(properties.getProperty("spring.security.user.name"),"${BASIC_AUTH_USER:user}");
   }
}

//@Test public void testIsPropertyPresent() throws Exception {
//   // Mock InputStream und Properties
//   InputStream mockInputStream = mock(InputStream.class);
//   Properties mockProperties = mock(Properties.class);
//   when(mockProperties.containsKey("api")).thenReturn(true);
//   when(mockProperties.containsKey("non_existing_property")).thenReturn(false);
//
//   // Mock SettingsController und setzen Sie den InputStream
//   SettingsController settingsController = new SettingsController();
//   SettingsController spySettingsController = org.mockito.Mockito.spy(settingsController);
//   org.mockito.Mockito.doReturn(mockInputStream).when(spySettingsController).getClass().getClassLoader().getResourceAsStream("application.properties");
//   org.mockito.Mockito.doReturn(mockProperties).when(spySettingsController).loadProperties(mockInputStream);
//
//   // Testen, ob die Eigenschaft vorhanden ist
//   assertTrue(spySettingsController.isPropertyPresent("api"));
//   assertFalse(spySettingsController.isPropertyPresent("non_existing_property"));
//}
//}

