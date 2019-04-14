package todolist.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps classes one into another.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Mapper {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Mapper.class);

    private static FrontItem staticItemToFrontItem(Item item) {
        return new FrontItem(
                item.getId(),
                item.getDescription(),
                item.getCreated(),
                item.isDone()
        );
    }

    private static Item staticFrontItemToItem(FrontItem front, User user) {
        var item = new Item();
        item.setId(front.getId());
        item.setDescription(front.getDescription());
        item.setCreated(front.getCreated());
        item.setDone(front.isDone());
        item.setUser(user);
        return item;
    }

    public List<FrontItem> itemToFrontItem(List<Item> items) {
        return items.stream()
                .map(Mapper::staticItemToFrontItem)
                .collect(Collectors.toList());
    }

    public FrontItem itemToFrontItem(Item item) {
        return staticItemToFrontItem(item);
    }

    public Item frontItemToItem(FrontItem front, User user) {
        return staticFrontItemToItem(front, user);
    }
}
