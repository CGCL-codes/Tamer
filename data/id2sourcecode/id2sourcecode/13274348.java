    public void remove(int index) {
        if (index >= listSize || index < 0) throw new IndexOutOfBoundsException();
        listSize--;
        if (index == listSize) {
            list[listSize] = null;
            return;
        }
        for (int i = index; i < listSize; i++) {
            list[i] = list[i + 1];
        }
        list[listSize] = null;
    }
