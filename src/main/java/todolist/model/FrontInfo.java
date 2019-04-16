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
    /**
     * Current logged user info.
     */
    private final String loggedUser;
    /**
     * Message about current operation.
     */
    private final String message;

    /**
     * Constructor.
     *
     * @param loggedUser Current logged user info.
     * @param message    Message about current operation.
     */
    public FrontInfo(String loggedUser, String message) {
        this.loggedUser = loggedUser;
        this.message = message;
    }
}
