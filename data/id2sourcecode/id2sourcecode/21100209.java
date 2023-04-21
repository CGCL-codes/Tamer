    private void remove(int index) {
        numModels--;
        for (int i = index; i < numModels; i++) models[i] = models[i + 1];
    }
