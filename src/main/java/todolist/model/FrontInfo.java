package todolist.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Object with information for frontend.
 * To use as JSON object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FrontInfo {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(FrontInfo.class);

    private final String loggedUser;

    private final String message;

    public FrontInfo(String loggedUser, String message) {
        this.loggedUser = loggedUser;
        this.message = message;
    }
}
