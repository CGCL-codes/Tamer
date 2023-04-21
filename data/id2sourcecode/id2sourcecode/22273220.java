        private static final double[] getDiscretePolarTarget(CIExyY blackxyY, CIEXYZ XYZ0, CIEXYZ XYZ1, CIEXYZ XYZ2, CIEXYZ XYZ3) {
            CIExyY[] xyYArray = new CIExyY[4];
            xyYArray[0] = new CIExyY(XYZ0);
            xyYArray[1] = new CIExyY(XYZ1);
            xyYArray[2] = new CIExyY(XYZ2);
            xyYArray[3] = new CIExyY(XYZ3);
            double d0 = Maths.delta(new double[] { xyYArray[0].x, xyYArray[0].y }, new double[] { xyYArray[1].x, xyYArray[1].y });
            double d1 = Maths.delta(new double[] { xyYArray[2].x, xyYArray[2].y }, new double[] { xyYArray[3].x, xyYArray[3].y });
            double d = (d0 + d1) / 2;
            double[] dxyValues = xyYArray[2].getDeltaxy(blackxyY);
            double[] polarValues = CIExyY.cartesian2polarCoordinatesValues(dxyValues[0], dxyValues[1]);
            polarValues[0] -= d;
            double[] targetValues = CIExyY.polar2cartesianCoordinatesValues(polarValues[0], polarValues[1]);
            targetValues = DoubleArray.plus(targetValues, new double[] { blackxyY.x, blackxyY.y });
            return targetValues;
        }
