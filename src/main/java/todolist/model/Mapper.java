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

    /**
     * Contains logic to convert single backend-item to frontend-item.
     *
     * @param item Backend-item.
     * @return FrontEnd-item.
     */
    private static FrontItem staticItemToFrontItem(Item item) {
        return new FrontItem(
                item.getId(),
                item.getDescription(),
                item.getCreated(),
                item.isDone()
        );
    }

    /**
     * Contains logic to convert frontend-item to backend-item.
     *
     * @param front Frontend-item.
     * @param user  User who created the item.
     * @return Backend-item.
     */
    private static Item staticFrontItemToItem(FrontItem front, User user) {
        var item = new Item();
        item.setId(front.getId());
        item.setDescription(front.getDescription());
        item.setCreated(front.getCreated());
        item.setDone(front.isDone());
        item.setUser(user);
        return item;
    }

    /**
     * Converts list of backend-item objects into list of frontend-item objects.
     *
     * @param items List of backend-items.
     * @return List of frontend-items.
     */
    public List<FrontItem> itemToFrontItem(List<Item> items) {
        return items.stream()
                .map(Mapper::staticItemToFrontItem)
                .collect(Collectors.toList());
    }

    /**
     * Converts single backend-item into frontend-item.
     *
     * @param item Backend-item.
     * @return Frontend-item.
     */
    public FrontItem itemToFrontItem(Item item) {
        return staticItemToFrontItem(item);
    }

    /**
     * Converts single frontend-item object to backend-item object.
     *
     * @param front Frontend-item.
     * @param user  User who created the item.
     * @return Backend item.
     */
    public Item frontItemToItem(FrontItem front, User user) {
        return staticFrontItemToItem(front, user);
    }
}
