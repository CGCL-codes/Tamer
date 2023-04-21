    public void remove(final int index) {
        for (int x = index; x < length - 1; x++) {
            data[x] = data[x + 1];
        }
        data[length - 1] = null;
        length--;
    }
