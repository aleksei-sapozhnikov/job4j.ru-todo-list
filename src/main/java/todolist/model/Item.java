package todolist.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import todolist.model.Item.Item;

import java.util.Objects;

<<<<<<<HEAD
=======

        >>>>>>>development
/**
 * Item model.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
        <<<<<<<HEAD
public class Item {
=======

    public class Item implements Comparable<Item> {
>>>>>>>development
        /**
         * Logger.
         */
        private static final Logger LOG = LogManager.getLogger(Item.class);

<<<<<<<HEAD
        private long id;
=======
        private int id;
>>>>>>>development
        private String description;
        private long created;
        private boolean done;

<<<<<<<HEAD

        public Item(long id, String description, long created, boolean done) {
            this.id = id;
            this.description = description;
            this.created = created;
=======
    public Item() {
            }

            @Override
            public String toString () {
                return String.format("Item{id=%d, description='%s', created=%d, done=%s}", id, description, created, done);
            }

            @Override
            public int compareTo (Item o){
                var result = this.id - o.id;
                return result == 0 ? result : (int) (this.created - o.created);
            }

            @Override
            public boolean equals (Object o){
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Item item = (Item) o;
                return id == item.id;
            }

            /**
             * Returns id.
             *
             * @return Value of id field.
             */
            public int getId () {
                return this.id;
            }

            public void setId ( int id){
                this.id = id;
            }

            /**
             * Returns description.
             *
             * @return Value of description field.
             */
            public String getDescription () {
                return this.description;
            }

            public void setDescription (String description){
                this.description = description;
            }

            /**
             * Returns created.
             *
             * @return Value of created field.
             */
            public long getCreated () {
                return this.created;
            }

            public void setCreated ( long created){
                this.created = created;
            }

            /**
             * Returns done.
             *
             * @return Value of done field.
             */
            public boolean isDone () {
                return this.done;
            }

            public void setDone ( boolean done){
>>>>>>>development
                this.done = done;
            }

            @Override
<<<<<<<HEAD
            public String toString () {
                return String.format("Item{id=%d, description='%s', created=%d, done=%s}", id, description, created, done);
=======
                public int hashCode () {
                    return Objects.hash(id);
>>>>>>>development
                }
}
