    public final void removeElementAt(int id) {
        if (id >= 0) {
            for (int i = id; i < current_item - 1; i++) items[i] = items[i + 1];
            items[current_item - 1] = new GeneralPath();
        } else items[0] = new GeneralPath();
        current_item--;
    }
