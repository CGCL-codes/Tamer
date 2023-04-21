    public void deleteValue(long value) throws InvalidValueException {
        int index = findValue(value);
        for (int i = index; i < numElements - 1; i++) {
            array[i] = array[i + 1];
        }
        numElements--;
    }
