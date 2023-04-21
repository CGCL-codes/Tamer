    public MultipleGradientPaintContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform t, RenderingHints hints, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace) throws NoninvertibleTransformException {
        boolean fixFirst = false;
        boolean fixLast = false;
        int len = fractions.length;
        if (fractions[0] != 0f) {
            fixFirst = true;
            len++;
        }
        if (fractions[fractions.length - 1] != 1f) {
            fixLast = true;
            len++;
        }
        for (int i = 0; i < fractions.length - 1; i++) if (fractions[i] == fractions[i + 1]) len--;
        this.fractions = new float[len];
        Color[] loColors = new Color[len - 1];
        Color[] hiColors = new Color[len - 1];
        normalizedIntervals = new float[len - 1];
        gradientUnderflow = colors[0].getRGB();
        gradientOverflow = colors[colors.length - 1].getRGB();
        int idx = 0;
        if (fixFirst) {
            this.fractions[0] = 0;
            loColors[0] = colors[0];
            hiColors[0] = colors[0];
            normalizedIntervals[0] = fractions[0];
            idx++;
        }
        for (int i = 0; i < fractions.length - 1; i++) {
            if (fractions[i] == fractions[i + 1]) {
                if (!colors[i].equals(colors[i + 1])) {
                    hasDiscontinuity = true;
                }
                continue;
            }
            this.fractions[idx] = fractions[i];
            loColors[idx] = colors[i];
            hiColors[idx] = colors[i + 1];
            normalizedIntervals[idx] = fractions[i + 1] - fractions[i];
            idx++;
        }
        this.fractions[idx] = fractions[fractions.length - 1];
        if (fixLast) {
            loColors[idx] = hiColors[idx] = colors[colors.length - 1];
            normalizedIntervals[idx] = 1 - fractions[fractions.length - 1];
            idx++;
            this.fractions[idx] = 1;
        }
        AffineTransform tInv = t.createInverse();
        double m[] = new double[6];
        tInv.getMatrix(m);
        a00 = (float) m[0];
        a10 = (float) m[1];
        a01 = (float) m[2];
        a11 = (float) m[3];
        a02 = (float) m[4];
        a12 = (float) m[5];
        this.cycleMethod = cycleMethod;
        this.colorSpace = colorSpace;
        if (cm.getColorSpace() == lrgbmodel_A.getColorSpace()) dataModel = lrgbmodel_A; else if (cm.getColorSpace() == srgbmodel_A.getColorSpace()) dataModel = srgbmodel_A; else throw new IllegalArgumentException("Unsupported ColorSpace for interpolation");
        calculateGradientFractions(loColors, hiColors);
        model = GraphicsUtil.coerceColorModel(dataModel, cm.isAlphaPremultiplied());
    }
