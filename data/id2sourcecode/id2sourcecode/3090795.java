    public void deleteColumn(int column) throws BadElementException {
        float newWidths[] = new float[--columns];
        for (int i = 0; i < column; i++) {
            newWidths[i] = widths[i];
        }
        for (int i = column; i < columns; i++) {
            newWidths[i] = widths[i + 1];
        }
        setWidths(newWidths);
        for (int i = 0; i < columns; i++) {
            newWidths[i] = widths[i];
        }
        widths = newWidths;
        Row row;
        int size = rows.size();
        for (int i = 0; i < size; i++) {
            row = (Row) rows.get(i);
            row.deleteColumn(column);
            rows.set(i, row);
        }
        if (column == columns) {
            curPosition.setLocation(curPosition.x + 1, 0);
        }
    }
