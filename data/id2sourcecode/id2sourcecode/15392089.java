    private void quick(long izq, long der) {
        long i = izq, j = der;
        long c = (izq + der) / 2;
        Grabable rc = rf.get(c);
        do {
            Grabable ri = rf.get(i);
            while (ri.compareTo(rc) < 0 && i < der) {
                i++;
                ri = rf.get(i);
            }
            Grabable rj = rf.get(j);
            while (rc.compareTo(rj) < 0 && j > izq) {
                j--;
                rj = rf.get(j);
            }
            if (i <= j) {
                rf.set(j, ri);
                rf.set(i, rj);
                i++;
                j--;
            }
        } while (i <= j);
        if (izq < j) quick(izq, j);
        if (i < der) quick(i, der);
    }
