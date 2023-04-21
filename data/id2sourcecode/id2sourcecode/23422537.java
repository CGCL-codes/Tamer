    public static int getNeuronNumberPerHiddenLayer(int inputNumber, int outputNumber) {
        int neuronNumberPerHiddenLayer = (int) (Math.round(Math.sqrt(inputNumber + outputNumber)) + getRandomValue(1, 10));
        if ((neuronNumberPerHiddenLayer < (inputNumber + outputNumber) / 20 + 1) || (neuronNumberPerHiddenLayer > (inputNumber + outputNumber) * 3 / 4)) {
            neuronNumberPerHiddenLayer = (inputNumber + outputNumber) / 2;
        }
        return neuronNumberPerHiddenLayer;
    }
