    private NodeProxy hasDescendantsInSet(int docIdx, NodeId ancestorId, int contextId, boolean includeSelf, boolean copyMatches) {
        int low = documentOffsets[docIdx];
        int high = low + (documentLengths[docIdx] - 1);
        int end = low + documentLengths[docIdx];
        int mid = 0;
        int cmp;
        NodeId id;
        while (low <= high) {
            mid = (low + high) / 2;
            id = nodes[mid].getNodeId();
            if (id.isDescendantOrSelfOf(ancestorId)) {
                break;
            }
            cmp = id.compareTo(ancestorId);
            if (cmp > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        if (low > high) {
            return null;
        }
        while (mid > documentOffsets[docIdx] && nodes[mid - 1].getNodeId().compareTo(ancestorId) >= 0) {
            --mid;
        }
        NodeProxy ancestor = new NodeProxy(nodes[documentOffsets[docIdx]].getDocument(), ancestorId, Node.ELEMENT_NODE);
        boolean foundOne = false;
        for (int i = mid; i < end; i++) {
            cmp = nodes[i].getNodeId().computeRelation(ancestorId);
            if (cmp > -1) {
                boolean add = true;
                if (cmp == NodeId.IS_SELF) {
                    add = includeSelf;
                }
                if (add) {
                    if (Expression.NO_CONTEXT_ID != contextId) {
                        ancestor.deepCopyContext(nodes[i], contextId);
                    } else {
                        ancestor.copyContext(nodes[i]);
                    }
                    if (copyMatches) ancestor.addMatches(nodes[i]);
                    foundOne = true;
                }
            } else {
                break;
            }
        }
        return foundOne ? ancestor : null;
    }
