    public void initId() {
        g.clear();
        int n = (in + out) / 2;
        for (int i = 1; i <= n; i++) {
            addEdge(i, 2 * n + 1 - i);
        }
    }
