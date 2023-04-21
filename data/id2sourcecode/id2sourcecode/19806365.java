    public void removePoint() {
        Integer ind = plotfit.getPointChosenIndex();
        if (ind != null) {
            int nsize = accm.length - 1;
            int iind = ind.intValue();
            double[] oldacc = accm;
            double[] oldphase = phasem;
            double[] newacc = new double[nsize];
            double[] newphase = new double[nsize];
            for (int i = 0; i < nsize; i++) {
                if (i < iind) {
                    newacc[i] = oldacc[i];
                    newphase[i] = oldphase[i];
                } else {
                    newacc[i] = oldacc[i + 1];
                    newphase[i] = oldphase[i + 1];
                }
            }
            analysis.xdata = newacc;
            analysis.ydata = newphase;
            accm = newacc;
            phasem = newphase;
        } else {
            System.out.println("No point found.");
        }
    }
