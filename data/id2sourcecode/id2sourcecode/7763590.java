    public static double[] intervalBisection(Parser funcParser, double seed1, double seed2, double precision) throws Exception {
        double x1 = seed1;
        double x2 = seed2;
        double x3;
        double y1;
        double y2;
        double y3;
        do {
            String[] vars = funcParser.getVariables();
            double[] x1s = { x1 };
            double[] x2s = { x2 };
            y1 = funcParser.getValue(x1s);
            y2 = funcParser.getValue(x2s);
            if (y1 * y2 > 0) throw new Exception(); else {
                x3 = (x1 + x2) / 2;
                double[] x3s = { x3 };
                y3 = funcParser.getValue(x3s);
                if (y1 * y3 > 0) x1 = x3; else x2 = x3;
            }
        } while (Math.abs(x2 - x1) > precision);
        double[] answer = { x1, x2 };
        return answer;
    }
