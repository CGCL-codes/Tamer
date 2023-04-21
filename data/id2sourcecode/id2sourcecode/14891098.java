    public Object getItemByLine(int line) {
        int s = -1;
        int e = itemCount();
        OutlineItem found = null;
        for (int idx = s + (e - s) / 2; e - s > 1; idx = s + (e - s) / 2) {
            OutlineItem item = getItem(idx);
            if (item.lno == line) return item;
            if (item.lno > line) {
                e = idx;
                continue;
            }
            if (item.lno < line) {
                found = item;
                s = idx;
                continue;
            }
        }
        return found;
    }
