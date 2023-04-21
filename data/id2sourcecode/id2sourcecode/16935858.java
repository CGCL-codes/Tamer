    public LinkedList splice() {
        if (spliced) return result;
        processing = true;
        int limit = elements;
        while (limit > 1 && processing) {
            int place = 0;
            for (int o = 0; o < limit - 1 && processing; o += 2) {
                LinkedList tempseq = new LinkedList();
                int tempsum = 0;
                DoubleMatcher matcher = new DoubleMatcher(data[o], data[o + 1], delims[o], delims[o + 1], ignoreWeight);
                addFailSafe(matcher);
                int[][] matches = matcher.match();
                removeFailSafe(matcher);
                if (matches.length == 3 && matches[0].length > 0 && processing) {
                    int mpos = 1;
                    int fcur = matches[0][0];
                    int weight = matches[2][0];
                    tempsum += weight;
                    double[] temp = new double[weight];
                    boolean done = false;
                    while (!done && processing) {
                        for (int t = 0; t < weight && processing; t++, fcur++) temp[t] = data[o][fcur];
                        tempseq.add(temp);
                        if (mpos < matches[0].length && processing) {
                            fcur = matches[0][mpos];
                            weight = matches[2][mpos];
                            tempsum += weight;
                            temp = new double[weight];
                            mpos++;
                        } else done = true;
                    }
                } else {
                    spliced = true;
                    return result;
                }
                int asize = tempsum;
                int cur = 0;
                double[] newB = new double[asize];
                boolean[] newD = makeEmptyDelimArray(asize);
                while (tempseq.size() > 0 && processing) {
                    double[] t = (double[]) tempseq.removeFirst();
                    for (int p = 0; p < t.length && processing; p++, cur++) newB[cur] = t[p];
                    if (tempseq.size() > 0) newD[cur] = true;
                }
                data[place] = newB;
                delims[place] = newD;
                place++;
                newB = null;
                newD = null;
                matches = null;
                matcher = null;
                tempseq = null;
            }
            limit = (limit + 1) / 2;
            if (place < limit && processing) {
                data[place] = data[elements - 1];
                delims[place] = delims[elements - 1];
            }
        }
        if (limit == 1 && processing) {
            int last = 0;
            int nextDelim = 0;
            while (nextDelim < data[0].length && processing) {
                while (nextDelim < data[0].length && !delims[0][nextDelim] && processing) nextDelim++;
                double[] t = new double[nextDelim - last];
                for (int i = 0; i < t.length && processing; i++) t[i] = data[0][last + i];
                result.add(t);
                last = nextDelim;
                nextDelim++;
            }
            if (last < data[0].length) {
                double[] t = new double[data[0].length - last];
                for (int i = 0; i < t.length && processing; i++) t[i] = data[0][last + i];
                result.add(t);
            }
        }
        spliced = true;
        processing = false;
        return result;
    }
