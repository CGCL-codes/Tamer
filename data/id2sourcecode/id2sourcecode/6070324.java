    private void removeChild(int i) throws SQLException {
        readAllRows();
        entryCount--;
        written = false;
        if (entryCount < 0) {
            Message.throwInternalError();
        }
        SearchRow[] newRows = PageStore.newSearchRows(entryCount);
        int[] newOffsets = MemoryUtils.newIntArray(entryCount);
        int[] newChildPageIds = new int[entryCount + 1];
        System.arraycopy(offsets, 0, newOffsets, 0, Math.min(entryCount, i));
        System.arraycopy(rows, 0, newRows, 0, Math.min(entryCount, i));
        System.arraycopy(childPageIds, 0, newChildPageIds, 0, i);
        if (entryCount > i) {
            System.arraycopy(rows, i + 1, newRows, i, entryCount - i);
            int startNext = i > 0 ? offsets[i - 1] : index.getPageStore().getPageSize();
            int rowLength = startNext - offsets[i];
            for (int j = i; j < entryCount; j++) {
                newOffsets[j] = offsets[j + 1] + rowLength;
            }
        }
        System.arraycopy(childPageIds, i + 1, newChildPageIds, i, entryCount - i + 1);
        offsets = newOffsets;
        rows = newRows;
        childPageIds = newChildPageIds;
        start -= CHILD_OFFSET_PAIR_LENGTH;
    }
