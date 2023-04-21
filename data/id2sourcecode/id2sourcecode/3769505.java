    public void removePoint() {
        Integer index = datapanel.getPointChosenIndex();
        if (index != null) {
            int newsize = sdata.length - 1;
            int iindex = index.intValue();
            double[] oldsdata = sdata;
            double[] olddata = data;
            double[] tempsdata = new double[newsize];
            double[] tempdata = new double[newsize];
            for (int i = 0; i < newsize; i++) {
                if (i < iindex) {
                    tempsdata[i] = oldsdata[i];
                    tempdata[i] = olddata[i];
                } else {
                    tempsdata[i] = oldsdata[i + 1];
                    tempdata[i] = olddata[i + 1];
                }
            }
            sdata = tempsdata;
            data = tempdata;
            plotData();
        }
    }
