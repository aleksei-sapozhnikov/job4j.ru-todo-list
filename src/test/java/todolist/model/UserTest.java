package todolist.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserTest {

    @Test
    public void testDefaultValues() {
        var user = new User();
        assertNull(user.getLogin());
        assertNull(user.getPassword());
    }

    @Test
    public void testSettersAndGetters() {
        var user = new User();
        user.setLogin("login user one");
        assertEquals(user.getLogin(), "login user one");
        user.setPassword("password user one");
        assertEquals(user.getPassword(), "password user one");
    }
}