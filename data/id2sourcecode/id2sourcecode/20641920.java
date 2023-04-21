    protected int findEdgeEqualRowIndex(VariablePath v, int nodeIdx, int rowFrom, int rowTo, boolean first) {
        int centerRow = rowFrom + (rowTo - rowFrom) / 2;
        PathNode[] currentRowNodes = getVariablePaths()[centerRow].getPathNodes();
        int valueNodeLen = v.getPathNodeLength();
        if (rowFrom >= rowTo) {
            if (currentRowNodes.length != valueNodeLen || currentRowNodes[nodeIdx].compareTo(v.getPathNode(nodeIdx)) != 0) return -1; else return rowFrom;
        } else {
            if (currentRowNodes.length > valueNodeLen) return findEdgeEqualRowIndex(v, nodeIdx, rowFrom, centerRow - 1, first); else if (currentRowNodes.length < valueNodeLen) return findEdgeEqualRowIndex(v, nodeIdx, centerRow + 1, rowTo, first); else {
                int compare = currentRowNodes[nodeIdx].compareTo(v.getPathNode(nodeIdx));
                if (compare < 0) return findEdgeEqualRowIndex(v, nodeIdx, centerRow + 1, rowTo, first); else if (compare > 0) return findEdgeEqualRowIndex(v, nodeIdx, rowFrom, centerRow - 1, first); else {
                    int moreCheckIdx = -1;
                    if (first) moreCheckIdx = findEdgeEqualRowIndex(v, nodeIdx, rowFrom, centerRow - 1, first); else moreCheckIdx = findEdgeEqualRowIndex(v, nodeIdx, centerRow + 1, rowTo, first);
                    return moreCheckIdx == -1 ? centerRow : moreCheckIdx;
                }
            }
        }
    }
