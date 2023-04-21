    public Object remove(int index) {
        validateBounds(index);
        Object val = array[index];
        tail--;
        for (int i = index; i < tail; i++) array[i] = array[i + 1];
        return val;
    }
