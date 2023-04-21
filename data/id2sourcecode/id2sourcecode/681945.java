    void BuildWavelet(double wavelength, double decay, int lnpoints) {
        wshcen = (lnpoints + 1) / 2;
        wshwidth = wshcen;
        double dsum = 0;
        for (int i = wshcen - wshwidth; i < wshcen + wshwidth + 1; i++) {
            double t = i - wshcen;
            double wval = t * Math.PI * 2 / wavelength;
            double cw = Math.cos(wval);
            double sw = Math.sin(wval);
            double tdec = t * decay;
            double dfac = Math.exp(-tdec * tdec);
            waveletsh[i] = dfac * cw;
            waveletsh_i[i] = dfac * sw;
            dsum += dfac;
        }
        if (dsum != 0.0) {
            for (int i = wshcen - wshwidth; i < wshcen + wshwidth + 1; i++) {
                waveletsh[i] /= dsum;
                waveletsh_i[i] /= dsum;
            }
        }
    }
