    public void removeLayerAt(int idx) {
        for (int i = idx; i < layers.length - 1; i++) {
            layers[i] = layers[i + 1];
        }
        layers[layers.length - 1] = null;
        layers = Arrays.copyOf(layers, layers.length - 1);
    }
