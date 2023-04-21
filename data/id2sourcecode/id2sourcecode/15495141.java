    public boolean removeValueAt(int index) {
        if (index >= size) return false;
        for (int i = index; i < size; i++) {
            list[i] = list[i + 1];
        }
        size--;
        return true;
    }
