    public double calc(double snrVal) {
        if (snrVal > snr[snr.length - 1]) return 0;
        int i1 = 0;
        int i2 = snr.length - 1;
        while (i2 - i1 > 1) {
            int i3 = (i1 + i2) / 2;
            if (snrVal > snr[i3]) i1 = i3; else i2 = i3;
        }
        return ber[i1] + (ber[i2] - ber[i1]) * (snrVal - snr[i1]) / (snr[i2] - snr[i1]);
    }
