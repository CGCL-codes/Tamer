    private static final void IntroSortLoopByNodeId(NodeProxy a[], int l, int r, int maxdepth) {
        while ((r - l) > M) {
            if (maxdepth <= 0) {
                HeapSort.sortByNodeId(a, l, r);
                return;
            }
            maxdepth--;
            int i = (l + r) / 2;
            int j;
            NodeProxy partionElement;
            if (a[l].getNodeId().compareTo(a[i].getNodeId()) > 0) SwapVals.swap(a, l, i);
            if (a[l].getNodeId().compareTo(a[r].getNodeId()) > 0) SwapVals.swap(a, l, r);
            if (a[i].getNodeId().compareTo(a[r].getNodeId()) > 0) SwapVals.swap(a, i, r);
            partionElement = a[i];
            i = l + 1;
            j = r - 1;
            while (i <= j) {
                while ((i < r) && (partionElement.getNodeId().compareTo(a[i].getNodeId()) > 0)) ++i;
                while ((j > l) && (partionElement.getNodeId().compareTo(a[j].getNodeId()) < 0)) --j;
                if (i <= j) {
                    SwapVals.swap(a, i, j);
                    ++i;
                    --j;
                }
            }
            if (l < j) IntroSortLoopByNodeId(a, l, j, maxdepth);
            if (i >= r) break;
            l = i;
        }
    }
