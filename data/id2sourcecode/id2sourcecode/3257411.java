    public Instance filterInstance(Instance tmpInstance) {
        double min = tmpInstance.getValue(0);
        double max = min;
        for (double d : tmpInstance.toArray()) {
            if (d > max) max = d;
            if (d < min) min = d;
        }
        double midrange = (max + min) / 2;
        double range = max - min;
        double[] instance = tmpInstance.toArray();
        for (int j = 0; j < instance.length; j++) {
            if (range < EPSILON) {
                instance[j] = normalMiddle;
            } else {
                instance[j] = ((instance[j] - midrange) / (range / normalRange)) + normalMiddle;
            }
        }
        return new SimpleInstance(instance, tmpInstance);
    }
