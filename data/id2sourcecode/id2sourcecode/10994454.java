    protected float branchPosition(PhyloNode n) {
        setAngle(n, 0);
        if (tree.isLeaf(n)) return 0;
        List<PhyloNode> children = tree.getChildrenOf(n);
        for (int i = 0; i < children.size(); i++) {
            PhyloNode child = children.get(i);
        }
        for (int i = 0; i < children.size(); i++) {
            PhyloNode child = (PhyloNode) children.get(i);
            branchPosition(child);
        }
        Collections.sort(children);
        PhyloNode loChild = (PhyloNode) Collections.min(children);
        PhyloNode hiChild = (PhyloNode) Collections.max(children);
        float stepSize = 1f / (numLeaves);
        float loLeaves = tree.getNumEnclosedLeaves(loChild);
        float hiLeaves = tree.getNumEnclosedLeaves(hiChild);
        float mLeaves = Math.max(loLeaves, hiLeaves);
        float loChildNewY = loChild.getTargetY() + (mLeaves - loLeaves) * stepSize / 2;
        float hiChildNewY = hiChild.getTargetY() - (mLeaves - hiLeaves) * stepSize / 2;
        float unscaledY = (loChildNewY + hiChildNewY) / 2;
        float unscaledX = calcXPosition(n);
        setPosition(n, unscaledX, unscaledY);
        return 0;
    }
