//////////
// ITEM
//////////

class Item {
    /** Id */
    id;
    /** Description */
    description;
    /** When it was created */
    created;
    /** Done or not? */
    done;

    /**
     * Item object constructor.
     *
     * @param {number} id Item id.
     * @param {string} description Item description.
     * @param {number} created Item creation time (milliseconds)
     * @param {boolean} done Boolean flag: done / notDone.
     */
    constructor(id, description, created, done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
    }
}

/**
 * Function to compare two items.
 *
 * First: undone item is less then done item.
 * Second: item created earlier is less than latter item.
 *
 * @param {Item} first First item.
 * @param {Item} second Second item.
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
 * @param {Array} itemsArray Array of items.
 * @param {Item} replacer Item which will replace existing item or will be added to array.
 * @return {Array}Array of items with replaced or added item.
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