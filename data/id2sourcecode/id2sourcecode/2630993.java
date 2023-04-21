    public void remove(int id) {
        if (id >= 0 && id <= size - 1) {
            for (int i = id; i < size - 1; i++) {
                vector[i] = vector[i + 1];
            }
            vector[size - 1] = null;
            size--;
        }
    }
