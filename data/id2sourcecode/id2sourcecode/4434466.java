    private double[] infoAvgContinuous(int lo, int hi, int a) {
        double splitInfo = 0;
        double total = hi - lo;
        HashMap<String, Integer> totalFreq = new HashMap<String, Integer>();
        HashMap<Double, HashMap<String, Integer>> freqAcum = new HashMap<Double, HashMap<String, Integer>>();
        for (Attribute att : classes) {
            totalFreq.put(((DiscreteAttribute) att).getValue(), 0);
        }
        for (int i = lo; i < hi; i++) {
            if (!(data.get(i).getAttribute(output) instanceof DiscreteAttribute)) throw new IllegalArgumentException("The output attribute must be discrete");
            String v = ((DiscreteAttribute) data.get(i).getAttribute(output)).getValue();
            if (totalFreq.get(v) == null) totalFreq.put(v, 0);
            totalFreq.put(v, totalFreq.get(v) + 1);
            double va = ((ContinuousAttribute) data.get(i).getAttribute(a)).getValue();
            if (freqAcum.get(va) == null) {
                freqAcum.put(va, new HashMap<String, Integer>());
                for (Attribute e : classes) {
                    if (i - 1 < 0) freqAcum.get(va).put(((DiscreteAttribute) e).getValue(), 0); else {
                        double pva = ((ContinuousAttribute) data.get(i - 1).getAttribute(a)).getValue();
                        freqAcum.get(va).put(((DiscreteAttribute) e).getValue(), freqAcum.get(pva).get(((DiscreteAttribute) e).getValue()));
                    }
                }
            }
            freqAcum.get(va).put(v, freqAcum.get(va).get(v) + 1);
        }
        double maxInfo = -Double.MIN_VALUE;
        double maxSplitInfo = -Double.MIN_VALUE;
        double bestSplitValue = Integer.MAX_VALUE;
        int bestIndex = -1000;
        for (int i = lo; i < hi; i++) {
            double value = ((ContinuousAttribute) data.get(i).getAttribute(a)).getValue();
            HashMap<String, Integer> freq = freqAcum.get(value);
            double total2 = i - lo + 1;
            double acum2 = 0;
            double total3 = total - total2;
            double acum3 = 0;
            for (String e : freq.keySet()) {
                int f = freq.get(e);
                if (f != 0) {
                    double p = (f / total2);
                    acum2 += -p * (Math.log10(p) / Math.log10(2));
                }
                f = totalFreq.get(e) - freq.get(e);
                if (f != 0) {
                    double p = f / total3;
                    acum3 += -p * (Math.log10(p) / Math.log10(2));
                }
            }
            double infoA = (total2 / total) * acum2;
            double infoB = (total3 / total) * acum3;
            splitInfo = 0;
            if ((int) total2 != 0) splitInfo += -(total2 / total) * (Math.log10(total2 / total) / Math.log10(2));
            if ((int) total3 != 0) splitInfo += -(total3 / total) * (Math.log10(total3 / total) / Math.log10(2));
            if (splitInfo > maxSplitInfo) {
                maxInfo = infoA + infoB;
                int k = 0;
                for (k = i + 1; k < hi; k++) {
                    double nextValue = ((ContinuousAttribute) data.get(k).getAttribute(a)).getValue();
                    if (value != nextValue) {
                        bestSplitValue = (value + nextValue) / 2;
                        bestIndex = k;
                        break;
                    }
                }
                if (k == hi) {
                    bestSplitValue = value;
                    bestIndex = hi - 1;
                }
                maxSplitInfo = splitInfo;
            }
        }
        return new double[] { maxInfo, maxSplitInfo, bestSplitValue, bestIndex };
    }
