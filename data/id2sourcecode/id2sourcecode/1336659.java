    public static Object financialNpv(Object[] args, XelContext ctx) {
        double[] d = UtilFns.toDoubleArray(UtilFns.toList(args, ctx));
        double[] values = new double[d.length - 1];
        for (int i = 0; i < values.length; i++) {
            values[i] = d[i + 1];
        }
        double result = npv(d[0], values);
        return new Double(result);
    }
