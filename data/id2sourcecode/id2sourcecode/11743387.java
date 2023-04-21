    public synchronized double getCalculated() {
        if (history.size() > STARTUP) {
            double last = history.getFirst();
            double mean1 = this.getMean(5);
            double mean2 = this.getMean(60);
            double mean3 = this.getMean();
            if (last > 1.1 * calculated) {
                calculated = Math.max(mean1, calculated);
                decreasing = 0;
            } else if (last > calculated) {
                calculated = last;
                decreasing = 0;
            } else if (calculated > mean1) {
                decreasing += Math.round(0.5 + calculated / mean1);
            }
            if (decreasing > 200) {
                double aux = Math.max(Math.max(mean1, mean2), mean3);
                if (required > aux) aux = (required + aux) / 2;
                calculated = (calculated + aux) / 2;
                decreasing /= 2;
            }
        } else {
            calculated = Math.max(this.getMean(5), required);
        }
        return calculated;
    }
