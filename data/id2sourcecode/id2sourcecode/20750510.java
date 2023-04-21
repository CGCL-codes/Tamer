    private void removeRow(int at) throws SQLException {
        readAllRows();
        index.getPageStore().logUndo(this, data);
        entryCount--;
        written = false;
        if (entryCount <= 0) {
            Message.throwInternalError();
        }
        int[] newOffsets = new int[entryCount];
        SearchRow[] newRows = new SearchRow[entryCount];
        System.arraycopy(offsets, 0, newOffsets, 0, at);
        System.arraycopy(rows, 0, newRows, 0, at);
        int startNext = at > 0 ? offsets[at - 1] : index.getPageStore().getPageSize();
        int rowLength = startNext - offsets[at];
        for (int j = at; j < entryCount; j++) {
            newOffsets[j] = offsets[j + 1] + rowLength;
        }
        System.arraycopy(rows, at + 1, newRows, at, entryCount - at);
        start -= OFFSET_LENGTH;
        offsets = newOffsets;
        rows = newRows;
    }
