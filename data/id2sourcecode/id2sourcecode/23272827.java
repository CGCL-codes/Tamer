    protected LookUpTable32LinearSRGBtoSRGB(int inMax, int outMax, double shadowCutoff, double shadowSlope, double scaleAfterExp, double exponent, double reduceAfterExp) {
        super(inMax + 1, outMax);
        int i = -1;
        double normalize = 1.0 / inMax;
        int cutOff = (int) Math.floor(shadowCutoff * inMax);
        shadowSlope *= outMax;
        int shift = (outMax + 1) / 2;
        for (i = 0; i <= cutOff; i++) lut[i] = (int) (Math.floor(shadowSlope * (i * normalize) + 0.5) - shift);
        scaleAfterExp *= outMax;
        reduceAfterExp *= outMax;
        for (; i <= inMax; i++) lut[i] = (int) (Math.floor(scaleAfterExp * Math.pow(i * normalize, exponent) - reduceAfterExp + 0.5) - shift);
    }
