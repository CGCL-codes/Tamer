    private void removeAt(short index) {
        size--;
        for (short i = index; i < size; i++) elements[i] = elements[i + 1];
    }
