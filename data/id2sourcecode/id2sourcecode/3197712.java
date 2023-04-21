    private void initCodeDynamic() {
        this.child = new int[TREESIZE];
        this.parent = new int[TREESIZE];
        this.block = new int[TREESIZE];
        this.edge = new int[TREESIZE];
        this.stock = new int[TREESIZE];
        this.sNode = new int[TREESIZE / 2];
        this.freq = new int[TREESIZE];
        n1 = (nMax >= (256 + maxMatch - THRESHOLD + 1)) ? 512 : nMax - 1;
        for (int i = 0; i < TREESIZE_CODE; ++i) {
            stock[i] = i;
            block[i] = 0;
        }
        int j = nMax * 2 - 2;
        for (int i = 0; i < nMax; ++i, --j) {
            freq[j] = 1;
            child[j] = ~i;
            sNode[i] = j;
            block[j] = 1;
        }
        avail = 2;
        edge[1] = nMax - 1;
        for (int i = nMax * 2 - 2; j >= 0; i -= 2, --j) {
            int f = freq[j] = freq[i] + freq[i - 1];
            child[j] = i;
            parent[i] = parent[i - 1] = j;
            if (f == freq[j + 1]) {
                block[j] = block[j + 1];
            } else {
                block[j] = stock[avail++];
            }
            edge[block[j]] = j;
        }
    }
