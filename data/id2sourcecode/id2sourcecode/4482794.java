    public int removeRandom(Forest f, int nEntries) {
        int nRemoved = 0;
        for (Node root : f.getTrees()) {
            for (int i = 0; i < nEntries; i++) {
                int nNodes = traverseTreePreorder(root, 0);
                if (nNodes <= 1) {
                    break;
                }
                int idx = 1 + randomIdx.nextInt(nNodes - 1);
                Node n = getNodeByPreorderIdx(root, new int[] { idx });
                Node parent = getParentOfPreorderIdx(root, null, new int[] { idx });
                if ((n != null) && (parent != null)) {
                    if (parent.getLeft() == n) {
                        parent.setLeft(null);
                    } else {
                        parent.setRight(null);
                    }
                    nRemoved++;
                }
            }
        }
        return nRemoved;
    }
