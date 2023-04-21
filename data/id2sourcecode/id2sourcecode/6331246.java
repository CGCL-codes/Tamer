    @Override
    public void swap(int i) {
        int temp = intArray[i];
        intArray[i] = intArray[i + 1];
        intArray[i + 1] = temp;
    }
