    public void setUpperBinarySearchLifetime(int upperBinarySearchLifetime) {
        this.upperBinarySearchLifetime = upperBinarySearchLifetime;
        binarySearchLifetime = (upperBinarySearchLifetime + lowerBinarySearchLifetime) / 2;
    }
