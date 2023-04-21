    public static double[] intervalBisection(Parser funcParser, String variable, double seed1, double seed2, double precision, double[] otherValues) throws Exception {
        double x1 = seed1;
        double x2 = seed2;
        double x3;
        double y1;
        double y2;
        double y3;
        do {
            String[] vars = funcParser.getVariables();
            double[] x1s = new double[vars.length];
            double[] x2s = new double[vars.length];
            int j = -1;
            for (int i = 0; i < vars.length; i++) {
                if (vars[i].equals(variable)) {
                    x1s[i] = x1;
                    x2s[i] = x2;
                } else {
                    j++;
                    x1s[i] = otherValues[j];
                    x2s[i] = otherValues[j];
                }
            }
            y1 = funcParser.getValue(x1s);
            y2 = funcParser.getValue(x2s);
            if (y1 * y2 > 0) throw new Exception(); else {
                x3 = (x1 + x2) / 2;
                double[] x3s = new double[vars.length];
                j = -1;
                for (int i = 0; i < vars.length; i++) {
                    if (vars[i].equals(variable)) {
                        x3s[i] = x3;
                    } else {
                        j++;
                        x3s[i] = otherValues[j];
                    }
                }
                y3 = funcParser.getValue(x3s);
                if (y1 * y3 > 0) x1 = x3; else x2 = x3;
            }
        } while (Math.abs(x2 - x1) > precision);
        double[] answer = { x1, x2 };
        return answer;
    }
