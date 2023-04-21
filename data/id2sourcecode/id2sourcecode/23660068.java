    private static final RBNode buildTree(Map.Entry[] arEntries, int iHeight, boolean boComplete, int iCurrentTier, int iStart, int iStop) {
        RBNode oNewTree;
        int iRootIndex;
        if (iStart == iStop) {
            return NIL;
        } else {
            iRootIndex = (iStop + iStart) / 2;
            oNewTree = new RBNode(arEntries[iRootIndex].getKey(), arEntries[iRootIndex].getValue());
            oNewTree._oLeft = buildTree(arEntries, iHeight, boComplete, (iCurrentTier + 1), iStart, iRootIndex);
            oNewTree._oRight = buildTree(arEntries, iHeight, boComplete, (iCurrentTier + 1), (iRootIndex + 1), iStop);
            if ((!boComplete) && ((iHeight % 2) == 1) && (iCurrentTier >= (iHeight - 2))) oNewTree._iColor = (iCurrentTier == (iHeight - 1)) ? RED : BLACK; else oNewTree._iColor = ((iCurrentTier % 2) == 1) ? RED : BLACK;
            if (oNewTree._oLeft != NIL) oNewTree._oLeft._oParent = oNewTree;
            if (oNewTree._oRight != NIL) oNewTree._oRight._oParent = oNewTree;
            return oNewTree;
        }
    }
