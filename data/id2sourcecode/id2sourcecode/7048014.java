    public Extremes getLocalExtremes(int windowsize) {
        Extremes extremes = new Extremes();
        if (windowsize >= this.getSize() || windowsize <= 2) return extremes;
        int size = (int) (windowsize / 2.);
        double window1[] = new double[size];
        double window2[] = new double[size];
        for (int i = 0; i < size; ++i) window1[i] = yvals[0];
        for (int i = 0; i < size; ++i) window2[i] = yvals[i + 1];
        double prev = -1;
        for (int i = 0; i < getSize(); ++i) {
            double mean1 = 0;
            for (int j = 1; j < size; ++j) mean1 += window1[j - 1] - window1[j];
            mean1 /= size;
            double mean2 = 0;
            for (int j = 1; j < size; ++j) mean2 += window2[j - 1] - window2[j];
            mean2 /= size;
            double val = (mean1 + mean2) / 2.;
            if (prev != -1 && prev > 0 && val < 0) extremes.minima.add(xvals[i - 1]); else if (prev != -1 && prev < 0 && val > 0) extremes.maxima.add(xvals[i - 1]);
            prev = val;
            for (int j = 1; j < size; ++j) {
                window1[j - 1] = window1[j];
                window2[j - 1] = window2[j];
            }
            window1[size - 1] = yvals[i];
            if (i + size >= getSize() - 1) window2[size - 1] = yvals[yvals.length - 1]; else window2[size - 1] = yvals[i + size + 1];
        }
        return extremes;
    }
