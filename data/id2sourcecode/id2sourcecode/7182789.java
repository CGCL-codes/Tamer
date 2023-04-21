    public void removeNeuronAt(int idx) {
        for (int i = idx; i < neurons.length - 1; i++) {
            neurons[i] = neurons[i + 1];
        }
        neurons[neurons.length - 1] = null;
        neurons = Arrays.copyOf(neurons, neurons.length - 1);
    }
