    private final float getMinSample(int x1, int x2) {
        if (x2 - x1 > reduction * 2) {
            x1 /= reduction;
            x2 /= reduction;
            float y = cachedMin.get(x1);
            for (int j = x1; j <= x2; j++) {
                float s = cachedMin.get(j);
                if (s < y) {
                    y = s;
                }
            }
            return y;
        } else {
            AChannel ch = getChannelModel();
            float y = ch.getSample(x1);
            for (int j = x1; j <= x2; j++) {
                float s = ch.getSample(j);
                if (s < y) {
                    y = s;
                }
            }
            return y;
        }
    }
