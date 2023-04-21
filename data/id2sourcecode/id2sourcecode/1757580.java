    public double[][] doubleToGrid(double[][] value) throws VisADException {
        if (value.length < DomainDimension) {
            throw new SetException("Gridded1DDoubleSet.doubleToGrid: value dimension " + value.length + " not equal to Domain dimension " + DomainDimension);
        }
        double[] vals = value[0];
        int length = vals.length;
        double[] samps = Samples[0];
        double[][] grid = new double[1][length];
        if (ig < 0 || ig >= LengthX) {
            ig = (LengthX - 1) / 2;
        }
        for (int i = 0; i < length; i++) {
            if (Double.isNaN(vals[i])) {
                grid[0][i] = Double.NaN;
            } else if (Length == 1) {
                grid[0][i] = 0;
            } else {
                int lower = 0;
                int upper = LengthX - 1;
                while (lower < upper) {
                    if ((vals[i] - samps[ig]) * (vals[i] - samps[ig + 1]) <= 0) break;
                    if (Ascending ? samps[ig + 1] < vals[i] : samps[ig + 1] > vals[i]) {
                        lower = ig + 1;
                    } else if (Ascending ? samps[ig] > vals[i] : samps[ig] < vals[i]) {
                        upper = ig;
                    }
                    if (lower < upper) ig = (lower + upper) / 2;
                }
                double solv = ig + (vals[i] - samps[ig]) / (samps[ig + 1] - samps[ig]);
                if (solv > -0.5 && solv < LengthX - 0.5) grid[0][i] = solv; else {
                    grid[0][i] = Double.NaN;
                    ig = (LengthX - 1) / 2;
                }
            }
        }
        return grid;
    }
