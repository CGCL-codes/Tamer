    private static int partition(Vector A, int p, int r) {
        int z = (r + p) / 2;
        String x = A.elementAt(z).toString();
        int i = p - 1;
        int j = r + 1;
        while (true) {
            while (x.compareTo(A.elementAt(--j).toString()) < 0) ;
            while (x.compareTo(A.elementAt(++i).toString()) > 0) ;
            if (i >= j) {
                return j;
            }
            Object o = A.elementAt(i);
            A.setElementAt(A.elementAt(j), i);
            A.setElementAt(o, j);
        }
    }
