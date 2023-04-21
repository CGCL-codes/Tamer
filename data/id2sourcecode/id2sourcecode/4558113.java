    public void init(Neuron neuron) {
        upperValue = neuron.getUpperBound();
        lowerValue = neuron.getLowerBound();
        middleValue = upperValue / lowerValue;
        lowerThreshold = (lowerValue + middleValue) / 2;
        upperThreshold = (middleValue + upperValue) / 2;
    }
