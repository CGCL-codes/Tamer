    public void remove(int idx) {
        count--;
        for (int i = idx; i < count; i++) {
            values[i] = values[i + 1];
            indexes[i] = indexes[i + 1];
            priorities[i] = priorities[i + 1];
        }
    }
