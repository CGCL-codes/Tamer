    SearchRow remove(SearchRow row) throws SQLException {
        int at = find(row, false, false, true);
        PageBtree page = index.getPage(childPageIds[at]);
        SearchRow last = page.remove(row);
        index.getPageStore().logUndo(this, data);
        updateRowCount(-1);
        written = false;
        if (last == null) {
            return null;
        } else if (last == row) {
            index.getPageStore().free(page.getPos(), true);
            if (entryCount < 1) {
                return row;
            }
            if (at == entryCount) {
                last = getRow(at - 1);
            } else {
                last = null;
            }
            removeChild(at);
            index.getPageStore().update(this);
            return last;
        }
        if (at == entryCount) {
            return last;
        }
        int child = childPageIds[at];
        removeChild(at);
        addChild(at, child, last);
        int temp = childPageIds[at];
        childPageIds[at] = childPageIds[at + 1];
        childPageIds[at + 1] = temp;
        index.getPageStore().update(this);
        return null;
    }
