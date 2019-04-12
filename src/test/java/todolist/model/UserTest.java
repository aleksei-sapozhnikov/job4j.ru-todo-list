package todolist.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserTest {

    @Test
    public void testDefaultValues() {
        var user = new User();
        assertEquals(user.getId(), 0);
        assertNull(user.getLogin());
        assertNull(user.getPassword());
    }

    @Test
    public void testSettersAndGetters() {
        var user = new User();
        user.setId(34);
        user.setLogin("login user one");
        user.setPassword("password user one");
        assertEquals(user.getId(), 34);
        assertEquals(user.getLogin(), "login user one");
        assertEquals(user.getPassword(), "password user one");
    }
}