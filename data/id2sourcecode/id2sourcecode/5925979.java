    public double[] map() {
        float[] pixels = (float[]) imp.getChannelProcessor().getPixels();
        double out[] = new double[pixels.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = pixels[i] / (pixels[i] + 1);
        }
        return out;
    }
