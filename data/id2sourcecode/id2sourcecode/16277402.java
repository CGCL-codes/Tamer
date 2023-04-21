    void setup(Tree[] trees, double[] indicatorParameter, double[] popSizes, boolean logSpace, boolean mid) {
        if (dirty) {
            if (type == VariableDemographicModel.Type.EXPONENTIAL) logSpace = false;
            boolean any = false;
            for (int nt = 0; nt < ti.length; ++nt) {
                if (setTreeTimes(nt, trees)) {
                    any = true;
                }
            }
            final int nd = indicatorParameter.length;
            assert nd == alltimes.length + (type == VariableDemographicModel.Type.STEPWISE ? -1 : 0) : " nd=" + nd + " alltimes.length=" + alltimes.length + " type=" + type;
            if (any) {
                int[] inds = new int[ttimes.length];
                for (int k = 0; k < alltimes.length; ++k) {
                    int j = 0;
                    while (inds[j] == ttimes[j].length) {
                        ++j;
                    }
                    for (int l = j + 1; l < inds.length; ++l) {
                        if (inds[l] < ttimes[l].length) {
                            if (ttimes[l][inds[l]] < ttimes[j][inds[j]]) {
                                j = l;
                            }
                        }
                    }
                    alltimes[k] = ttimes[j][inds[j]];
                    inds[j]++;
                }
            }
            int tot = 1;
            for (int k = 0; k < nd; ++k) {
                if (indicatorParameter[k] > 0) {
                    ++tot;
                }
            }
            times = new double[tot + 1];
            values = new double[tot];
            intervals = new double[tot - 1];
            times[0] = 0.0;
            times[tot] = Double.POSITIVE_INFINITY;
            final boolean xx = type == VariableDemographicModel.Type.LINEAR && !logSpace && false;
            if (xx) {
                double[] a = alltimes;
                if (mid) {
                    a = new double[alltimes.length];
                    for (int k = 0; k < a.length; ++k) {
                        a[k] = ((alltimes[k] + (k > 0 ? alltimes[k - 1] : 0)) / 2);
                    }
                }
                bestLinearFit(a, popSizes, indicatorParameter, times, values);
                for (int n = 0; n < intervals.length; ++n) {
                    intervals[n] = times[n + 1] - times[n];
                }
                for (int n = 0; n < values.length; ++n) {
                    if (values[n] <= 0) {
                        values[n] = 1e-30;
                    }
                }
            }
            if (!xx) {
                values[0] = logSpace ? Math.exp(popSizes[0]) : popSizes[0];
                int n = 0;
                for (int k = 0; k < nd && n + 1 < tot; ++k) {
                    if (indicatorParameter[k] > 0) {
                        times[n + 1] = mid ? ((alltimes[k] + (k > 0 ? alltimes[k - 1] : 0)) / 2) : alltimes[k];
                        values[n + 1] = logSpace ? Math.exp(popSizes[k + 1]) : popSizes[k + 1];
                        intervals[n] = times[n + 1] - times[n];
                        ++n;
                    }
                }
            }
            dirty = false;
        }
    }
