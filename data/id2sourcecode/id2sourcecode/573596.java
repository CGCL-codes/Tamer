    private int weight2Y(int weight) {
        int result;
        if (maxWeight != minWeight) {
            result = minHeight + (int) ((weight - minWeight) * (maxHeight - minHeight + 0.0) / (maxWeight - minWeight + 0.0));
        } else {
            result = (maxHeight + minHeight) / 2;
        }
        return result;
    }
