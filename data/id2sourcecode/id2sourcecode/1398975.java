    public boolean remove(T object) {
        for (int i = 0; i < size; i++) {
            if (items[i] == object) {
                for (int j = i; j < size - 1; j++) {
                    items[j] = items[j + 1];
                }
                size--;
                return true;
            }
        }
        return false;
    }
