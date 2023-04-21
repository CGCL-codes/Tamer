    public double[] map() {
        double LOG05 = -0.693147;
        int length = imp.getWidth() * imp.getHeight();
        double[][] out = new double[imp.getHeight()][imp.getWidth()];
        float[] pixels = (float[]) imp.getChannelProcessor().getPixels();
        float[] luminance = calculateLuminance(length, pixels);
        double maxLum = luminance[1];
        double avLum = luminance[0];
        float[][] pixels2D = ArrayTools.convert1D2D(pixels, imp.getHeight(), imp.getWidth());
        maxLum /= avLum;
        double divider = Math.log10(maxLum + 1.0);
        double biasP = Math.log(bias) / LOG05;
        for (int y = 0; y < imp.getHeight(); y++) {
            for (int x = 0; x < imp.getWidth(); x++) {
                double Yw = pixels2D[y][x] / avLum;
                double interpol = Math.log(2.0 + biasFunc(biasP, Yw / maxLum) * 8.0);
                out[y][x] = (Math.log(Yw + 1.0) / interpol) / divider;
            }
        }
        for (int x = 0; x < imp.getWidth(); x++) {
            for (int y = 0; y < imp.getHeight(); y++) {
                double scale = out[y][x] / pixels2D[y][x];
                pixels2D[y][x] *= scale;
                if (pixels2D[y][x] == 1.0) {
                    System.out.println(scale);
                    System.out.println(pixels2D[y][x]);
                    System.out.println(x + " " + y);
                }
            }
        }
        return ArrayTools.convert2D1D(pixels2D, imp.getHeight(), imp.getWidth());
    }
