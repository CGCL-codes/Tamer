        NodeProxy hasDescendantsInSet(NodeId ancestorId, int contextId, boolean includeSelf) {
            int low = 0;
            int high = length - 1;
            int mid = 0;
            int cmp;
            NodeId id;
            while (low <= high) {
                mid = (low + high) / 2;
                id = array[mid].getNodeId();
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
            while (mid > 0 && array[mid - 1].getNodeId().compareTo(ancestorId) >= 0) {
                --mid;
            }
            NodeProxy ancestor = new NodeProxy(getDocument(), ancestorId, Node.ELEMENT_NODE);
            boolean foundOne = false;
            for (int i = mid; i < length; i++) {
                cmp = array[i].getNodeId().computeRelation(ancestorId);
                if (cmp > -1) {
                    boolean add = true;
                    if (cmp == NodeId.IS_SELF) {
                        add = includeSelf;
                    }
                    if (add) {
                        if (Expression.NO_CONTEXT_ID != contextId) {
                            ancestor.deepCopyContext(array[i], contextId);
                        } else {
                            ancestor.copyContext(array[i]);
                        }
                        ancestor.addMatches(array[i]);
                        foundOne = true;
                    }
                } else {
                    break;
                }
            }
            return foundOne ? ancestor : null;
        }
