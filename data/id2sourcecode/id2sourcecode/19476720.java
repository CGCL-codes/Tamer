    private static void siftdownByNodeId(NodeProxy[] a, int n, int vacant, NodeProxy missing, int drop) {
        int memo = vacant;
        int count = 0;
        int next_peek = (drop + 1) / 2;
        int child = 2 * (vacant + 1);
        NodeId missingNodeId = missing.getNodeId();
        while (child < n) {
            if (a[child].getNodeId().compareTo(a[child - 1].getNodeId()) < 0) child--;
            a[vacant] = a[child];
            vacant = child;
            child = 2 * (vacant + 1);
            count++;
            if (count == next_peek) {
                if (a[(vacant - 1) / 2].getNodeId().compareTo(missingNodeId) <= 0) break; else next_peek = (count + drop + 1) / 2;
            }
        }
        if (child == n) {
            a[vacant] = a[n - 1];
            vacant = n - 1;
        }
        int parent = (vacant - 1) / 2;
        while (vacant > memo) {
            if (a[parent].getNodeId().compareTo(missingNodeId) < 0) {
                a[vacant] = a[parent];
                vacant = parent;
                parent = (vacant - 1) / 2;
            } else break;
        }
        a[vacant] = missing;
    }
