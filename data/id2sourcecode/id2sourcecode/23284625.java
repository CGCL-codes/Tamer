    @Override
    public void setColumn(int i, Vector column) {
        if (i >= columns || i < 0) throw new IndexOutOfBoundsException();
        for (int ii = 0; ii < column.length(); ii++) {
            int position = rowPointers[ii], limit = rowPointers[ii + 1];
            while (position < limit && columnIndices[position] < i) position++;
            if (Math.abs(column.get(ii)) > EPS) {
                if (columnIndices[position] != i) {
                    if (values.length < nonzero + 1) {
                        growup();
                    }
                    for (int k = nonzero; k > position; k--) {
                        values[k] = values[k - 1];
                        columnIndices[k] = columnIndices[k - 1];
                    }
                }
                for (int k = ii + 1; k < rows + 1; k++) {
                    rowPointers[k]++;
                }
                values[position] = column.get(ii);
                columnIndices[position] = i;
                nonzero++;
            } else if (columnIndices[position] == i && position < limit) {
                for (int k = position; k < nonzero - 1; k++) {
                    values[k] = values[k + 1];
                    columnIndices[k] = columnIndices[k + 1];
                }
                for (int k = ii + 1; k < rows + 1; k++) {
                    rowPointers[k]--;
                }
                nonzero--;
            }
        }
    }
