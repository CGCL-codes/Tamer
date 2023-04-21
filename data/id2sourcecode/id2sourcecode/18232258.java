    private void quicksortInternal(int[] indices, int begin, int end) {
        if (begin >= end) return;
        int j = end;
        int i = begin;
        int mitte = (i + j) / 2;
        Object mittelElement = tableModel.getValueAt(mitte, columnNumber);
        while (j >= i) {
            while (compare(tableModel.getValueAt(i, columnNumber), mittelElement) < 0) i++;
            while (compare(tableModel.getValueAt(j, columnNumber), mittelElement) > 0) j--;
            if (i <= j) {
                int x = indices[i];
                indices[i] = indices[j];
                indices[j] = x;
                i++;
                j--;
            }
        }
        quicksortInternal(indices, begin, j);
        quicksortInternal(indices, i, end);
    }
