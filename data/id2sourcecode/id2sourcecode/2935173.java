    public static PathNode buildPath(FileLine[] orig, FileLine[] rev, Cancellable cancellable, boolean ignoreLeadingSpaces) {
        if (orig == null) {
            throw new IllegalArgumentException("original sequence is null");
        }
        if (rev == null) {
            throw new IllegalArgumentException("revised sequence is null");
        }
        final int N = orig.length;
        final int M = rev.length;
        final int MAX = N + M + 1;
        final int size = 1 + 2 * MAX;
        final int middle = (size + 1) / 2;
        final PathNode diagonal[] = new PathNode[size];
        diagonal[middle + 1] = new Snake(0, -1, null);
        for (int d = 0; d < MAX; d++) {
            if (cancellable.isCancelled()) {
                return null;
            }
            for (int k = -d; k <= d; k += 2) {
                if (cancellable.isCancelled()) {
                    return null;
                }
                final int kmiddle = middle + k;
                final int kplus = kmiddle + 1;
                final int kminus = kmiddle - 1;
                PathNode prev = null;
                int i;
                if ((k == -d) || (k != d && diagonal[kminus].i < diagonal[kplus].i)) {
                    i = diagonal[kplus].i;
                    prev = diagonal[kplus];
                } else {
                    i = diagonal[kminus].i + 1;
                    prev = diagonal[kminus];
                }
                diagonal[kminus] = null;
                int j = i - k;
                PathNode node = new DiffNode(i, j, prev);
                while (i < N && j < M && orig[i].matches(rev[j], ignoreLeadingSpaces)) {
                    i++;
                    j++;
                }
                if (i > node.i) node = new Snake(i, j, node);
                diagonal[kmiddle] = node;
                if (i >= N && j >= M) {
                    return diagonal[kmiddle];
                }
            }
            diagonal[middle + d - 1] = null;
        }
        return null;
    }
