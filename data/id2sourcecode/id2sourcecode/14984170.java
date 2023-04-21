    private RGB getRBByLumi(final CIExyY targetxyY, boolean relativeXYZ, double tolerance, double targetValue, RGBBase.Channel ch, boolean whiteRGB) {
        double initStep = INIT_STEP;
        if (maxLuminanceConstraint == -1) {
            this.luminanceConstraint = this.lcdModel.getLuminance().Y;
        } else {
            this.luminanceConstraint = maxLuminanceConstraint;
            initStep = 1;
        }
        touchMaxIterativeTime = false;
        RGB minDeltaGRGB = null;
        double absDelta = Double.MAX_VALUE;
        double minDelta = Double.MAX_VALUE;
        double minDeltaE = Double.MAX_VALUE;
        CIExyY clone = (CIExyY) targetxyY.clone();
        this.targetChannel = ch;
        double dEIndex = Double.MAX_VALUE;
        for (int x = 0; x < MAX_ITERATIVE_TIME && (absDelta > tolerance || dEIndex > .25); x++, initStep *= 2) {
            RGB rgb = getRB0(clone, relativeXYZ, initStep, targetValue, tolerance, whiteRGB, ch);
            double estimate = 0;
            if (whiteRGB) {
                estimate = rgb.getValue(rgb.getMaxChannel(), RGB.MaxValue.Double255);
            } else {
                estimate = rgb.getValue(ch, RGB.MaxValue.Double255);
            }
            absDelta = Math.abs(estimate - targetValue);
            DeltaE ciede = getDeltaE(rgb, targetxyY, relativeXYZ);
            dEIndex = getDeltaIndex(ciede);
            if (absDelta < minDelta && dEIndex < minDeltaE) {
                minDelta = absDelta;
                minDeltaE = dEIndex;
                minDeltaGRGB = rgb;
            }
            clone.Y *= 0.9;
            if (whiteRGB && luminanceConstraint != -1) {
                luminanceConstraint *= 0.99;
            }
            if (x == (MAX_ITERATIVE_TIME - 1)) {
                touchMaxIterativeTime = true;
            }
        }
        return minDeltaGRGB;
    }
