    public VDdemographicFunction(double[] t, double[] p, Type units, VariableDemographicModel.Type type) {
        super(units);
        this.type = type;
        final int tot = p.length;
        times = new double[tot + 1];
        values = p;
        intervals = new double[tot - 1];
        times[0] = 0.0;
        times[tot] = Double.POSITIVE_INFINITY;
        System.arraycopy(t, 0, times, 1, t.length);
        for (int n = 0; n < intervals.length; ++n) {
            intervals[n] = times[n + 1] - times[n];
        }
        dirty = false;
    }
