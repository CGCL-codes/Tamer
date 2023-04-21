    public void addEntry(double t, Object v) {
        if (t == Double.POSITIVE_INFINITY) return;
        int lo = 0;
        int hi = this.size();
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            TimedEntry e = (TimedEntry) this.elementAt(mid);
            if (t > e.t) lo = mid + 1; else {
                if (t < e.t) hi = mid; else lo = hi = mid;
            }
        }
        try {
            TimedEntry e = new TimedEntry(t, v);
            this.insertElementAt(e, lo);
        } catch (Exception exc) {
            throw new UnexpectedTQException();
        }
    }
