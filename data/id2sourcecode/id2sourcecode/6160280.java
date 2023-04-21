    private LookupTableJAI createColorMappingLUT() {
        ColorModel colorModel = this.getCorrectedImageColorModel();
        ColorSpace colorSpace = colorModel.getColorSpace();
        int[] componentSizes = colorModel.getComponentSize();
        ColorCurve valueCurve = null;
        ColorCurve[] componentCurves = new ColorCurve[componentSizes.length];
        boolean[] applyValueCurve = new boolean[componentSizes.length];
        for (int n = 0; n < componentSizes.length; n++) {
            applyValueCurve[n] = false;
        }
        if (channelMap != null) {
            valueCurve = channelMap.getChannelCurve("value");
            switch(colorSpace.getType()) {
                case ColorSpace.TYPE_GRAY:
                    componentCurves[0] = valueCurve;
                    break;
                case ColorSpace.TYPE_RGB:
                    componentCurves[0] = channelMap.getChannelCurve("red");
                    componentCurves[1] = channelMap.getChannelCurve("green");
                    componentCurves[2] = channelMap.getChannelCurve("blue");
                    applyValueCurve[0] = true;
                    applyValueCurve[1] = true;
                    applyValueCurve[2] = true;
                    break;
                default:
                    break;
            }
        }
        if (valueCurve == null) {
            valueCurve = new ColorCurve();
        }
        LookupTableJAI jailut = null;
        if (componentSizes[0] == 8) {
            byte[][] lut = new byte[componentSizes.length][256];
            double dx = 1.0 / 256.0;
            for (int band = 0; band < colorModel.getNumComponents(); band++) {
                for (int n = 0; n < lut[band].length; n++) {
                    double x = dx * n;
                    double val = x;
                    if (band < componentCurves.length && componentCurves[band] != null) {
                        val = componentCurves[band].getValue(val);
                    }
                    if (band < applyValueCurve.length && applyValueCurve[band]) {
                        val = valueCurve.getValue(val);
                    }
                    val = Math.max(0.0, Math.min(val, 1.0));
                    lut[band][n] = (byte) ((lut[band].length - 1) * val);
                }
            }
            jailut = new LookupTableJAI(lut);
        } else if (componentSizes[0] == 16) {
            short[][] lut = new short[componentSizes.length][0x10000];
            double dx = 1.0 / 65536.0;
            for (int band = 0; band < colorModel.getNumComponents(); band++) {
                for (int n = 0; n < lut[band].length; n++) {
                    double x = dx * n;
                    double val = x;
                    if (band < componentCurves.length && componentCurves[band] != null) {
                        val = componentCurves[band].getValue(val);
                    }
                    if (band < applyValueCurve.length && applyValueCurve[band]) {
                        val = valueCurve.getValue(val);
                    }
                    val = Math.max(0.0, Math.min(val, 1.0));
                    lut[band][n] = (short) ((lut[band].length - 1) * val);
                }
            }
            jailut = new LookupTableJAI(lut, true);
        } else {
            log.error("Unsupported data type with with = " + componentSizes[0]);
        }
        return jailut;
    }
