    private Object remove(int remove) {
        Object removed = values[remove];
        for (int i = remove; i < size; i++) {
            if (i == headers.length - 1) {
                headers[i] = null;
                values[i] = null;
            } else {
                headers[i] = headers[i + 1];
                values[i] = values[i + 1];
            }
        }
        if (remove < size) size--;
        return removed;
    }
