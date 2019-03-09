package todolist.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void testDefaultValues() {
        var item = new Item();
        assertEquals(item.getId(), 0);
        assertNull(item.getDescription());
        assertEquals(item.getCreated(), 0);
        assertFalse(item.isDone());
    }

    @Test
    public void testSettersAndGetters() {
        var item = new Item();
        item.setId(34);
        item.setDescription("item number one");
        item.setCreated(97870);
        item.setDone(true);
        assertEquals(item.getId(), 34);
        assertEquals(item.getDescription(), "item number one");
        assertEquals(item.getCreated(), 97870);
        assertTrue(item.isDone());
    }

    @Test
    public void testToString() {
        var item = this.createItem(11, "item", 34, true);
        assertEquals(item.toString(),
                String.format("Item{id=%d, description='%s', created=%d, done=%s}", 11, "item", 34, true));
    }

    @Test
    public void testEqualsHashcode() {
        var main = this.createItem(11, "22", 33, false);
        var copy = this.createItem(11, "22", 33, false);
        var otherId = this.createItem(24233, "22", 33, false);
        var otherDescription = this.createItem(11, "other", 33, false);
        var otherCreated = this.createItem(11, "22", 32451, false);
        var otherDone = this.createItem(11, "22", 33, true);
        // not equal
        assertNotEquals(main, null);
        assertNotEquals(main, "main");
        assertNotEquals(main, otherId);
        // equal
        assertEquals(main, main);
        assertSame(main.hashCode(), main.hashCode());
        assertEquals(main, copy);
        assertSame(main.hashCode(), copy.hashCode());
        assertEquals(main, otherDescription);
        assertSame(main.hashCode(), otherDescription.hashCode());
        assertEquals(main, otherCreated);
        assertSame(main.hashCode(), otherCreated.hashCode());
        assertEquals(main, otherDone);
        assertSame(main.hashCode(), otherDone.hashCode());
    }

    private Item createItem(int id, String description, long created, boolean done) {
        var item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setCreated(created);
        item.setDone(done);
        return item;
    }
}