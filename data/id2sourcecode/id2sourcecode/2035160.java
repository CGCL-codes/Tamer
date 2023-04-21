    public static Double average(Double avg, Double float1) {
        Double rtnAvg = avg;
        if (rtnAvg == null) {
            rtnAvg = float1;
        }
        rtnAvg = (rtnAvg + float1) / 2;
        return rtnAvg;
    }
