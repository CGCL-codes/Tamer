    public void removeCharAt(int index) {
        if (index < 0 || index > used) {
            throw new IndexOutOfBoundsException("" + index);
        }
        used--;
        for (int i = index; i < used; i++) {
            array[i] = array[i + 1];
        }
    }
