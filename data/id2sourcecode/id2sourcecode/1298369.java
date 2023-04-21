    private static int getInsertionPos(Vector vect, TreeNode node, VirtualTreeCommands comparer) {
        int size = vect.size();
        int begin, end, middle, begRes, midRes, endRes;
        if (size == 0) return 0;
        begin = 0;
        end = size - 1;
        begRes = comparer.vtCompareNodes(node, (TreeNode) vect.elementAt(begin));
        endRes = comparer.vtCompareNodes(node, (TreeNode) vect.elementAt(end));
        if (begRes < 0) return 0;
        if (endRes > 0) return size;
        for (; ; ) {
            middle = (begin + end) / 2;
            midRes = comparer.vtCompareNodes(node, (TreeNode) vect.elementAt(middle));
            if (((midRes <= 0) && (endRes >= 0)) || ((midRes >= 0) && (endRes <= 0))) {
                begin = middle;
                begRes = midRes;
            } else {
                end = middle;
                endRes = midRes;
            }
            if ((end - begin) <= 1) {
                if (begRes < 0) return begin; else return end;
            }
        }
    }
