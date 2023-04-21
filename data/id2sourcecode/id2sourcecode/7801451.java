    public double[] getLogOdds() {
        lods = new double[pwm.length];
        int i = 0;
        while (i < pwm.length / 4) {
            double inf = 0;
            int j = 0;
            while (j < 4) {
                if (pwm[i * 4 + j] > 0) {
                    inf += pwm[i * 4 + j] * Math.log(pwm[i * 4 + j]) / Math.log(2);
                }
                j++;
            }
            inf = (2 + inf) / 2;
            j = 0;
            while (j < 4) {
                if (pwm[i * 4 + j] > 0) {
                    lods[i * 4 + j] = (Math.log(pwm[i * 4 + j] / 0.24) / Math.log(2)) * inf;
                } else {
                    lods[i * 4 + j] = (Math.log(.0001) / Math.log(2)) * inf;
                }
                j++;
            }
            i++;
        }
        return lods;
    }
