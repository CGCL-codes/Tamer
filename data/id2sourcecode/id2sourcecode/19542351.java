    public double getMedian() {
        double result;
        if (data.isEmpty()) throw new IllegalStateException("Data vector is empty!");
        Collections.sort(data);
        if (data.size() % 2 != 0) {
            result = data.get(data.size() / 2);
        } else {
            double a = data.get(data.size() / 2);
            double b = data.get(data.size() / 2 - 1);
            result = (a + b) / 2;
        }
        return result;
    }
