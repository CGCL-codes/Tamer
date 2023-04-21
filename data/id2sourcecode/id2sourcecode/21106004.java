    private void removeRow(int i) throws SQLException {
        index.getPageStore().logUndo(this, data);
        written = false;
        readAllRows();
        Row r = rows[i];
        if (r != null) {
            memorySize += r.getMemorySize();
        }
        entryCount--;
        if (entryCount < 0) {
            Message.throwInternalError();
        }
        firstOverflowPageId = 0;
        overflowRowSize = 0;
        rowRef = null;
        int keyOffsetPairLen = 2 + data.getVarLongLen(keys[i]);
        int[] newOffsets = new int[entryCount];
        long[] newKeys = new long[entryCount];
        Row[] newRows = new Row[entryCount];
        System.arraycopy(offsets, 0, newOffsets, 0, i);
        System.arraycopy(keys, 0, newKeys, 0, i);
        System.arraycopy(rows, 0, newRows, 0, i);
        int startNext = i > 0 ? offsets[i - 1] : index.getPageStore().getPageSize();
        int rowLength = startNext - offsets[i];
        for (int j = i; j < entryCount; j++) {
            newOffsets[j] = offsets[j + 1] + rowLength;
        }
        System.arraycopy(keys, i + 1, newKeys, i, entryCount - i);
        System.arraycopy(rows, i + 1, newRows, i, entryCount - i);
        start -= keyOffsetPairLen;
        offsets = newOffsets;
        keys = newKeys;
        rows = newRows;
    }
