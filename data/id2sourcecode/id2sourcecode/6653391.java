    double[] rgbToColorTemp(double rgb[]) {
        double Tmax;
        double Tmin;
        double testRGB[] = null;
        Tmin = 2000;
        Tmax = 12000;
        double T;
        for (T = (Tmax + Tmin) / 2; Tmax - Tmin > 10; T = (Tmax + Tmin) / 2) {
            testRGB = colorTempToRGB(T);
            if (testRGB[2] / testRGB[0] > rgb[2] / rgb[0]) Tmax = T; else Tmin = T;
        }
        double green = (testRGB[1] / testRGB[0]) / (rgb[1] / rgb[0]);
        double result[] = { T, green };
        return result;
    }
