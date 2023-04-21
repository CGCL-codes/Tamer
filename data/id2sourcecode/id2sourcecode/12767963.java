    protected double remove(double element) {
        int index = insertionPoint(element);
        double value = getMembership(element);
        if ((index > 0 && support[index] == element) || (index <= 0 && support[0] == element)) {
            for (int i = index; i < size - 1; i++) {
                support[i] = support[i + 1];
                membership[i] = membership[i + 1];
            }
            size--;
        }
        return value;
    }
