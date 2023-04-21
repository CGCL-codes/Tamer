    public static int findLastIndex(RowKeyIndex table, int startIndex, int endIndex) {
        int rowCount = table.getRowCount();
        if (rowCount >= 0) {
            if (rowCount < endIndex) endIndex = rowCount;
        }
        if (table.isRowAvailable(endIndex - 1)) return endIndex;
        final int old = table.getRowIndex();
        try {
            while (startIndex < endIndex) {
                int middle = (startIndex + endIndex) / 2;
                assert (middle != endIndex) : "something is grossly wrong with integer division";
                table.setRowIndex(middle);
                if (table.isRowAvailable()) startIndex = middle + 1; else endIndex = middle;
            }
            return endIndex;
        } finally {
            table.setRowIndex(old);
        }
    }
