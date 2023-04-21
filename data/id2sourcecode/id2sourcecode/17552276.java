    public PathNode buildPath(Object[] orig, Object[] rev) throws DifferentiationFailedException {
        int N;
        int M;
        int MAX;
        int size;
        int middle;
        Map<Integer, PathNode> diagonal;
        PathNode d_kminus;
        PathNode d_kplus;
        PathNode path;
        PathNode node;
        int kmiddle;
        int kplus;
        int kminus;
        PathNode prev;
        int i;
        int j;
        long startTime;
        if (orig == null) {
            throw new IllegalArgumentException("original sequence is null");
        }
        if (rev == null) {
            throw new IllegalArgumentException("revised sequence is null");
        }
        N = orig.length;
        M = rev.length;
        MAX = N + M + 1;
        size = 1 + 2 * MAX;
        middle = (size + 1) / 2;
        diagonal = new HashMap<Integer, PathNode>();
        path = null;
        startTime = System.currentTimeMillis();
        diagonal.put(middle + 1, new Snake(0, -1, null));
        for (int d = 0; d < MAX; d++) {
            if (checkMaxTime && System.currentTimeMillis() - startTime > MAXTIME) {
                throw new org.jmeld.diff.MaxTimeExceededException("Algoritm is taking up to much time");
            }
            for (int k = -d; k <= d; k += 2) {
                kmiddle = middle + k;
                kplus = kmiddle + 1;
                kminus = kmiddle - 1;
                prev = null;
                d_kminus = diagonal.get(kminus);
                d_kplus = diagonal.get(kplus);
                if ((k == -d) || (k != d && d_kminus.i < d_kplus.i)) {
                    i = d_kplus.i;
                    prev = d_kplus;
                } else {
                    i = d_kminus.i + 1;
                    prev = d_kminus;
                }
                diagonal.remove(kminus);
                j = i - k;
                node = new DiffNode(i, j, prev);
                while (i < N && j < M && orig[i].equals(rev[j])) {
                    i++;
                    j++;
                }
                if (i > node.i) {
                    node = new Snake(i, j, node);
                }
                diagonal.put(kmiddle, node);
                if (i >= N && j >= M) {
                    return diagonal.get(kmiddle);
                }
            }
            diagonal.put(middle + d - 1, null);
        }
        throw new DifferentiationFailedException("could not find a diff path");
    }
