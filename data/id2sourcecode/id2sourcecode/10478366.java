    @Override
    public void filter(Instance instance) {
        double min = instance.value(0);
        double max = min;
        for (Double d : instance) {
            if (d > max) max = d;
            if (d < min) min = d;
        }
        double midrange = (max + min) / 2;
        double range = max - min;
        for (int i = 0; i < instance.noAttributes(); i++) {
            if (range < EPSILON) {
                instance.put(i, normalMiddle);
            } else {
                instance.put(i, ((instance.value(i) - midrange) / (range / normalRange)) + normalMiddle);
            }
        }
    }
