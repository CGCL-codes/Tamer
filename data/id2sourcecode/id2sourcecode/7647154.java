    private BigInt treeSum(int v, int tl, int tr, int l, int r) {
        if (l > r) return new BigInt(0);
        if (l == tl && r == tr) return tree.get(v);
        int tm = (tl + tr) / 2;
        return treeSum(v << 1, tl, tm, l, Math.min(r, tm)).add(treeSum((v << 1) + 1, tm + 1, tr, Math.max(l, tm + 1), r));
    }
