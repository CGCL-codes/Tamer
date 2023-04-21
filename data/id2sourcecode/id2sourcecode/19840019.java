    public void removePoint() {
        Integer index = datapanel.getPointChosenIndex();
        if (index != null) {
            int newsize = xphase.length - 1;
            int iindex = index.intValue();
            double[] oldxphase = xphase;
            double[] oldyvalues = currentyvalues;
            double[] tempxdata = new double[newsize];
            double[] tempdata = new double[newsize];
            for (int i = 0; i < newsize; i++) {
                if (i < iindex) {
                    tempxdata[i] = oldxphase[i];
                    tempdata[i] = oldyvalues[i];
                } else {
                    tempxdata[i] = oldxphase[i + 1];
                    tempdata[i] = oldyvalues[i + 1];
                }
            }
            xphase = tempxdata;
            currentyvalues = tempdata;
            dataHasBeenFit = false;
            plotData();
        }
    }
