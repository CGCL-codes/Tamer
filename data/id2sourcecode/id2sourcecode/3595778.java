    public void remove(int idx) {
        int i;
        size--;
        for (i = idx; i < size; i++) {
            data[i] = data[i + 1];
        }
    }
