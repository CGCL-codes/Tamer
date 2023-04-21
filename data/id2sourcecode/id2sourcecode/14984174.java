        public double function(double[] Y) {
            iterativexyY.Y = Y[0];
            CIEXYZ resultXYZ = CIExyY.toXYZ(iterativexyY);
            RGB rgb = adapter.getRGB(resultXYZ, targetRelativeXYZ, true);
            double estimate = 0;
            if (whiteRGBMode) {
                estimate = rgb.getValue(rgb.getMaxChannel(), RGB.MaxValue.Double255);
            } else {
                estimate = rgb.getValue(targetChannel, RGB.MaxValue.Double255);
            }
            double delta = Math.abs(targetValue - estimate);
            DeltaE dE = getDeltaE(rgb, iterativexyY, targetRelativeXYZ);
            double result = getDeltaIndex(dE) + delta;
            return result;
        }
