    public static void calculate(FloatSampleBuffer fsb, float[][] buffer) {
        int width = buffer.length;
        int length = 0;
        if (width > 0 && fsb != null) {
            if (fsb.getChannelCount() > 0) {
                length = fsb.getSampleCount();
            }
        }
        calculate(fsb, buffer, 0, length);
    }
