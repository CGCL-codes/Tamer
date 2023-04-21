    private LookupTableJAI createSaturationMappingLUT() {
        ColorModel colorModel = this.getCorrectedImageColorModel();
        int[] componentSizes = colorModel.getComponentSize();
        ColorCurve satCurve = null;
        if (channelMap != null) {
            satCurve = channelMap.getChannelCurve("saturation");
        }
        if (satCurve == null) {
            satCurve = new ColorCurve();
        }
        LookupTableJAI jailut = null;
        if (componentSizes[0] == 8) {
            byte[][] lut = new byte[componentSizes.length][256];
            double dx = 1.0 / 256.0;
            for (int band = 0; band < componentSizes.length; band++) {
                for (int n = 0; n < lut[band].length; n++) {
                    double x = dx * n;
                    double val = x;
                    if (band == 2) {
                        val = satCurve.getValue(val);
                    }
                    val = Math.max(0.0, Math.min(val, 1.0));
                    lut[band][n] = (byte) ((lut[band].length - 1) * val);
                }
            }
            jailut = new LookupTableJAI(lut);
        } else if (componentSizes[0] == 16) {
            short[][] lut = new short[componentSizes.length][0x10000];
            double dx = 1.0 / 65536.0;
            for (int band = 0; band < componentSizes.length; band++) {
                for (int n = 0; n < lut[band].length; n++) {
                    double x = dx * n;
                    double val = x;
                    if (band == 2) {
                        val = satCurve.getValue(val);
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
