    public final void removeElementAt(int id) {
        if (id >= 0) {
            for (int i = id; i < max_size - 2; i++) items[i] = items[i + 1];
            items[max_size - 1] = false;
        } else items[0] = false;
        current_item--;
    }
