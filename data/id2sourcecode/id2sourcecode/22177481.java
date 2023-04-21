    public StatisticalCategory(HistogramDataSet histo, double from, double to) {
        this.histogram = histo;
        this.from = from;
        this.to = to;
        this.center = (to + from) / 2;
    }
