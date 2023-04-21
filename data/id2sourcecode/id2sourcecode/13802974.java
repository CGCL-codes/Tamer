    protected float branchPosition(CDAONodeView n) throws Exception {
        setAngle(n, 0);
        if (tree.isLeaf(n)) {
            return 0;
        }
        List<CDAONodeViewImpl> children = tree.getChildren(n);
        for (int i = 0; i < children.size(); i++) {
            CDAONodeView child = (CDAONodeView) children.get(i);
            branchPosition(child);
        }
        Collections.sort(children);
        CDAONodeView loChild = (CDAONodeView) Collections.min(children);
        CDAONodeView hiChild = (CDAONodeView) Collections.max(children);
        float stepSize = 1f / (numLeaves);
        float loLeaves = tree.getNumEnclosedLeaves(loChild);
        float hiLeaves = tree.getNumEnclosedLeaves(hiChild);
        float mLeaves = Math.max(loLeaves, hiLeaves);
        float loChildNewY = loChild.getY() + (mLeaves - loLeaves) * stepSize / 2;
        float hiChildNewY = hiChild.getY() - (mLeaves - hiLeaves) * stepSize / 2;
        float unscaledY = (loChildNewY + hiChildNewY) / 2;
        float unscaledX = calcXPosition(n);
        setPosition(n, unscaledX, unscaledY);
        return 0;
    }
