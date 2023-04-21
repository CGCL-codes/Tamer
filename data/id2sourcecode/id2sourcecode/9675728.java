    public final synchronized boolean remove(ElementGroup group) {
        for (int i = 0; i < layerSize; i++) {
            if (params[i] == group) {
                for (int j = i; j < topLayer; j++) {
                    params[j] = params[j + 1];
                }
                layerSize--;
                topLayer = layerSize - 1;
                return true;
            }
        }
        return false;
    }
