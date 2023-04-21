    public static Image createPattern(double orientation, double frequency, double size, double aspect, double phase, int color0, int color1, int width, int height) {
        double[] pixels = new double[width * height];
        int patternsize = (int) (size * 15);
        double[] pattern = new double[patternsize * patternsize];
        for (int x = 0; x < patternsize; x++) for (int y = 0; y < patternsize; y++) pattern[x + y * patternsize] = gaborRot(x - patternsize / 2, y - patternsize / 2, orientation, frequency, size, aspect, phase);
        int nspatter = 1000;
        for (int i = 0; i < nspatter; i++) {
            int ctrx = (int) (width * Math.random());
            int ctry = (int) (height * Math.random());
            addTo(pixels, width, height, ctrx, ctry, pattern, patternsize, patternsize);
        }
        int[] pix = new int[width * height];
        for (int i = 0; i < pix.length; i++) {
            double mult = pixels[i];
            mult = (mult + 1) / 2;
            if (mult > 1) mult = 1;
            if (mult < 0) mult = 0;
            int red = interp(mult, (color0 >> 16) & 255, (color1 >> 16) & 255);
            int green = interp(mult, (color0 >> 8) & 255, (color1 >> 8) & 255);
            int blue = interp(mult, (color0 >> 0) & 255, (color1 >> 0) & 255);
            pix[i] = (255 << 24) | (red << 16) | (green << 8) | (blue << 0);
        }
        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(width, height, pix, 0, width));
    }
