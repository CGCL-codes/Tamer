        public CIELCh getBoundaryLCh(final CIELCh D65LCh) {
            CIELCh test = (CIELCh) D65LCh.clone();
            if (test.L == 0 || test.L == 100) {
                return test;
            }
            if (onLineCalculate) {
                while (!isOutOfGamut(test)) {
                    test.C *= 1.5;
                }
                double max = test.C;
                double min = D65LCh.C;
                do {
                    test.C = (max + min) / 2;
                    if (!isOutOfGamut(test)) {
                        min = test.C;
                    } else {
                        max = test.C;
                    }
                } while ((max - min) > threshold);
                test.C = max;
            } else {
                double[] XYZValues = getXYZValues(D65LCh);
                double[] rgbValues = pcs.fromCIEXYZValues(XYZValues);
                rgbValues = RGB.rationalize(rgbValues, RGB.MaxValue.Double1);
                double[] chroma = ti.getValues(rgbValues);
                test.C = chroma[0];
            }
            return test;
        }
