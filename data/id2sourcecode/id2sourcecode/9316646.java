    private int addContentSpecNodes(int begin, int end) {
        if (begin == end) {
            return fGroupIndexStack[fDepth][begin];
        }
        int middle = (begin + end) / 2;
        return addContentSpecNode(fOpStack[fDepth], addContentSpecNodes(begin, middle), addContentSpecNodes(middle + 1, end));
    }
