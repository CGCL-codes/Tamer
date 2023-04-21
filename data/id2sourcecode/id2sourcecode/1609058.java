    public static double genererValeurLoiNormale(double min, double max, double var) {
        double moy = (min + max) / 2;
        double dev = (max - moy) / var;
        return genererValeurLoiNormale(moy, dev);
    }
