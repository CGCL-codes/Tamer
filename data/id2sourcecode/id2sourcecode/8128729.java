    private int bsearch(int l, int h, int tot, int costs) {
        int lo = l;
        int hi = h;
        while (lo <= hi) {
            int cur = (lo + hi) / 2;
            int ot = ((node) nodes.elementAt(cur)).total;
            if ((tot < ot) || (tot == ot && costs >= ((node) nodes.elementAt(cur)).costs)) hi = cur - 1; else lo = cur + 1;
        }
        return lo;
    }
