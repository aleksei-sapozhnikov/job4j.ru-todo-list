/**
 * Returns array of items stored on server or error message.
 *
 * @param servletAddress Servlet returning items.
 * @param method Http method to use.
 * @return {Object} Received array of items or error.
 */
function getItems(servletAddress, method) {
    let result = {error: 'Could not get list of objects from server'};
    $.ajax({
        type: method,
        async: false,
        datatype: "application/json",
        url: servletAddress,
        success: function (response) {
            if (response.error != null) {
                alert(response.error);
            } else {
                result = JSON.parse(response);
            }
        }
    });
    return result;
}

/**
 * Sends given item to server and returns result.
 *
 * @param item Item to save or update.
 * @param servletAddress Servlet handling request.
 * @param method Http method to use.
 * @return {Object} Item received from server as response, or error object.
 */
function sendItem(item, servletAddress, method) {
    let result = {error: 'Response item not received'};
    $.ajax({
        type: method,
        async: false,
        data: JSON.stringify(item),
        url: servletAddress,
        success: function (response) {
            if (response.error != null) {
                alert(response.error);
            } else {
                result = JSON.parse(response);
            }
        }
    });
    return result;
}