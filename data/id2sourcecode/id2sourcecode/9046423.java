    private static KernelJAI createGaussKernel(final int size) {
        final float[] data = new float[size * size];
        final int max = (size + 1) / 2;
        final int min = max - size;
        int c = 0;
        for (int j = min; j < max; j++) {
            final int j2 = j * j;
            for (int i = min; i < max; i++) {
                final int i2 = i * i;
                data[c++] = (float) exp(-(sqrt(i2 + j2)));
            }
        }
        normalize(data);
        return new KernelJAI(size, size, data);
    }
