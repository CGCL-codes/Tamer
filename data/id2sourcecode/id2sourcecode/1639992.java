    public static float[][] calculate(FloatSampleBuffer fsb, int width, int height) {
        float[][] retVal = new float[fsb.getChannelCount()][width];
        calculate(fsb, retVal);
        return retVal;
    }
