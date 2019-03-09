/* * * * * * * * * * * * * * * *
 * BUILD ITEMS TABLE HTML CODE
 * * * * * * * * * * * * * * * */

/**
 * Function drawing table of existing items
 *
 * @param items Array of existing items to show in table.
 * @param isShowAll Flag. True means show items which are done,
 * False means not to show.
 * @return {string} Html code of table with items.
 */
function constructItemsTable(items, isShowAll) {
    let html = '';
    html += '<table id="existing-items-table" class="table table-bordered">';
    html += constructItemsTableHead();
    html += '<tbody style="text-align:center">';
    items.forEach(function (item) {
        if (isShowAll || !(item.done)) {
            html += constructItemsTableOneItemRow(item);
        }
    });
    html += '</tbody>';
    html += "</table>";
    return html;
}

/**
 * Function drawing head of the items table.
 *
 * @return {string} Html code of the head.
 */
function constructItemsTableHead() {
    let html = '';
    html += '<thead style="text-align:center;">';
    html += '<tr>';
    html += '<th>ID</th>';
    html += '<th>Description</th>';
    html += '<th>Created</th>';
    html += '<th>Done</th>';
    html += '</tr>';
    html += '</thead>';
    return html;
}

/**
 * Constructs one row (one item) in the items table.
 *
 * @param item Item to print.
 * @return {string} HTML code of the row.
 */
function constructItemsTableOneItemRow(item) {
    let html = '';
    html += '<tr>';
    html += '<td>' + item.id + '</td>';
    html += '<td>' + item.description + '</td>';
    let date = new Date(item.created).toLocaleString();
    html += `<td>${date}</td>`;
    html += `<td><input name="${item.id}" type="checkbox" ${item.done ? 'checked' : ''}></td>`;
    html += '</tr>';
    return html;
}