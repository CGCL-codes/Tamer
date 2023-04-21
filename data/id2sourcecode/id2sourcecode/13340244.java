    public int compareTo(Object o) {
        TemperleyLiebTerm ot = (TemperleyLiebTerm) o;
        if (this.in != ot.in) {
            return (this.in - ot.in);
        } else if (this.out != ot.out) {
            return (this.out - ot.out);
        }
        if (hasCrossings() || ot.hasCrossings()) {
            int n = (in + out) / 2;
            int[][] pairs = new int[n][2];
            int i = 0;
            Iterator<Edge> i1 = g.iterator();
            Iterator<Edge> i2 = ot.g.iterator();
            Edge e1, e2;
            int e1min, e1max, e2min, e2max;
            while (i1.hasNext() && i2.hasNext()) {
                do {
                    e1 = i1.next();
                } while (e1.isTrivial() && i1.hasNext());
                do {
                    e2 = i2.next();
                } while (e2.isTrivial() && i2.hasNext());
                if (e1.getMin() != e2.getMin()) {
                    return e1.getMin() - e2.getMin();
                }
                if (e1.getMax() != e2.getMax()) {
                    return e1.getMax() - e2.getMax();
                }
            }
            if (i1.hasNext()) {
                return 1;
            } else if (i2.hasNext()) {
                return -1;
            } else {
                return 0;
            }
        }
        Long thisVal = Long.valueOf(toParenString());
        Long otVal = Long.valueOf(ot.toParenString());
        return thisVal.compareTo(otVal);
    }
