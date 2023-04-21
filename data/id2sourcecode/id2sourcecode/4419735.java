    @Override
    public boolean remove(R element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(set[i])) {
                for (; i < size - 1; i++) {
                    set[i] = set[i + 1];
                }
                size--;
            }
        }
        return false;
    }
