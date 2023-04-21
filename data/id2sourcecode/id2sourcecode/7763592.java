    public static double[] intervalBisection(ComplexParser funcParser, int part, double seed1, double seed2, double precision, double otherValue) throws Exception {
        double x1 = seed1;
        double x2 = seed2;
        double x3;
        double w1;
        double w2;
        double w3;
        do {
            double[][] z1s = new double[1][2];
            double[][] z2s = new double[1][2];
            z1s[0][part] = x1;
            z2s[0][part] = x2;
            z1s[0][1 - part] = otherValue;
            z2s[0][1 - part] = otherValue;
            int j = -1;
            w1 = funcParser.getRealValue(z1s);
            w2 = funcParser.getRealValue(z2s);
            if (w1 * w2 > 0) throw new Exception(); else {
                x3 = (x1 + x2) / 2;
                double[][] z3s = new double[1][2];
                z3s[0][part] = x3;
                z3s[0][1 - part] = otherValue;
                w3 = funcParser.getRealValue(z3s);
                if (w1 * w3 > 0) x1 = x3; else x2 = x3;
            }
        } while (Math.abs(x2 - x1) > precision);
        double[] answer = { x1, x2 };
        return answer;
    }
