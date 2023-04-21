    public void Sort(Object[] list, SortTool tool, boolean descending) {
        final int stackSize = 32;
        StackItem[] stack = new StackItem[stackSize];
        for (int n = 0; n < 32; ++n) stack[n] = new StackItem();
        int stackPtr = 0;
        int comp;
        if (descending) comp = SortTool.COMP_GRTR; else comp = SortTool.COMP_LESS;
        final int Threshold = 7;
        int lsize, rsize;
        int l, r, mid, scanl, scanr, pivot;
        l = 0;
        r = list.length - 1;
        Object temp;
        while (true) {
            while (r > l) {
                if ((r - l) > Threshold) {
                    mid = (l + r) / 2;
                    if (tool.compare(list[mid], list[l]) == comp) {
                        temp = list[mid];
                        list[mid] = list[l];
                        list[l] = temp;
                    }
                    if (tool.compare(list[r], list[l]) == comp) {
                        temp = list[r];
                        list[r] = list[l];
                        list[l] = temp;
                    }
                    if (tool.compare(list[r], list[mid]) == comp) {
                        temp = list[mid];
                        list[mid] = list[r];
                        list[r] = temp;
                    }
                    pivot = r - 1;
                    temp = list[mid];
                    list[mid] = list[pivot];
                    list[pivot] = temp;
                    scanl = l + 1;
                    scanr = r - 2;
                } else {
                    pivot = r;
                    scanl = l;
                    scanr = r - 1;
                }
                for (; ; ) {
                    while ((tool.compare(list[scanl], list[pivot]) == comp) && (scanl < r)) ++scanl;
                    while ((tool.compare(list[pivot], list[scanr]) == comp) && (scanr > l)) --scanr;
                    if (scanl >= scanr) break;
                    temp = list[scanl];
                    list[scanl] = list[scanr];
                    list[scanr] = temp;
                    if (scanl < r) ++scanl;
                    if (scanr > l) --scanr;
                }
                temp = list[scanl];
                list[scanl] = list[pivot];
                list[pivot] = temp;
                lsize = scanl - l;
                rsize = r - scanl;
                if (lsize > rsize) {
                    if (lsize != 1) {
                        ++stackPtr;
                        if (stackPtr == stackSize) throw err1;
                        stack[stackPtr].left = l;
                        stack[stackPtr].right = scanl - 1;
                    }
                    if (rsize != 0) l = scanl + 1; else break;
                } else {
                    if (rsize != 1) {
                        ++stackPtr;
                        if (stackPtr == stackSize) throw err1;
                        stack[stackPtr].left = scanl + 1;
                        stack[stackPtr].right = r;
                    }
                    if (lsize != 0) r = scanl - 1; else break;
                }
            }
            if (stackPtr != 0) {
                l = stack[stackPtr].left;
                r = stack[stackPtr].right;
                --stackPtr;
            } else break;
        }
    }
