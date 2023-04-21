    public static void calculate(FloatSampleBuffer fsb, float[][] buffer, int start, int length) {
        int width = buffer[0].length;
        if (length > 0) {
            int nSamplesPerPixel = length / width;
            for (int c = 0; c < fsb.getChannelCount(); c++) {
                float data[] = fsb.getChannel(c);
                for (int i = 0; i < width; i++) {
                    float value = 0;
                    for (int j = 0; j < nSamplesPerPixel; j++) {
                        value += Math.abs(data[start + i * nSamplesPerPixel + j]);
                    }
                    value /= nSamplesPerPixel;
                    buffer[c][i] = value;
                }
            }
        } else {
            for (int c = 0; c < fsb.getChannelCount(); c++) {
                for (int i = 0; i < width; i++) {
                    buffer[c][i] = 0.0f;
                }
            }
        }
    }
