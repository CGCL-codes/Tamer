    @Override
    public void swap(int i) {
        double temp = doubleArray[i];
        doubleArray[i] = doubleArray[i + 1];
        doubleArray[i + 1] = temp;
    }
