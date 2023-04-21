    public void remove(int index) {
        if ((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException();
        }
        --size;
        for (int i = index; i < size; ++i) {
            array[i] = array[i + 1];
        }
        array[size] = null;
    }
