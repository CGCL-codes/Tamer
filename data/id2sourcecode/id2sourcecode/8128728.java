    private int rbsearch(int l, int h, int tot, int costs) {
        if (l > h) return l;
        int cur = (l + h) / 2;
        int ot = ((node) nodes.elementAt(cur)).total;
        if ((tot < ot) || (tot == ot && costs >= ((node) nodes.elementAt(cur)).costs)) return rbsearch(l, cur - 1, tot, costs);
        return rbsearch(cur + 1, h, tot, costs);
    }
