/*  * * * * * * * * * * *
* COMMON ITEMS FUNCTIONS
* * * * * * * * * * * * */

/**
 * Item object constructor.
 *
 * @param id Item id.
 * @param description Item description.
 * @param created Item creation time (milliseconds)
 * @param done Boolean flag: done/notDone/
 * @constructor
 */
function Item(id, description, created, done) {
    this.id = id;
    this.description = description;
    this.created = created;
    this.done = done;
}

/**
 * Function to compare two items.
 *
 * First: undone item is less then done item.
 * Second: item created earlier is less than latter item.
 *
 * @param first First item.
 * @param second Second item.
 * @return {number} Positive if first > second, negative otherwise.
 */
function compareItems(first, second) {
    let result = 0;
    if (first.done && !second.done) {
        result = 1;
    } else if (!first.done && second.done) {
        result = -1
    } else {
        result = first.created - second.created;
    }
    return result;
}

/**
 * Replaces item in array to replacer, if item's id is equal to replacer's id.
 * If not found item with needed id, then adds replacer to array.
 *
 * @param itemsArray Array of items.
 * @param replacer Item which will replace existing item or will be added to array.
 * @return Array of items with replaced or added item.
 */
function replaceOrAddItemIntoArray(itemsArray, replacer) {
    let iOld = itemsArray.findIndex(function (elt) {
        return elt.id === replacer.id;
    });
    if (iOld !== -1) {
        itemsArray[iOld] = replacer;
    } else {
        itemsArray.push(replacer);
    }
    return itemsArray;
}