        NodeSet getDescendantsInSet(NodeSet result, NodeProxy parent, boolean childOnly, boolean includeSelf, int mode, int contextId, boolean copyMatches) {
            NodeProxy p;
            NodeId parentId = parent.getNodeId();
            if (parentId == NodeId.DOCUMENT_NODE) {
                for (int i = 0; i < length; i++) {
                    boolean add;
                    if (childOnly) {
                        add = array[i].getNodeId().getTreeLevel() == 1;
                    } else if (includeSelf) {
                        add = true;
                    } else {
                        add = array[i].getNodeId() != NodeId.DOCUMENT_NODE;
                    }
                    if (add) {
                        switch(mode) {
                            case NodeSet.DESCENDANT:
                                if (Expression.NO_CONTEXT_ID != contextId) {
                                    array[i].deepCopyContext(parent, contextId);
                                } else {
                                    array[i].copyContext(parent);
                                }
                                if (copyMatches) array[i].addMatches(parent);
                                result.add(array[i]);
                                break;
                            case NodeSet.ANCESTOR:
                                if (Expression.NO_CONTEXT_ID != contextId) {
                                    parent.deepCopyContext(array[i], contextId);
                                } else {
                                    parent.copyContext(array[i]);
                                }
                                if (copyMatches) parent.addMatches(array[i]);
                                result.add(parent, 1);
                                break;
                        }
                    }
                }
            } else {
                int low = 0;
                int high = length - 1;
                int mid = 0;
                int cmp;
                while (low <= high) {
                    mid = (low + high) / 2;
                    p = array[mid];
                    if (p.getNodeId().isDescendantOrSelfOf(parentId)) {
                        break;
                    }
                    cmp = p.getNodeId().compareTo(parentId);
                    if (cmp > 0) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
                if (low > high) {
                    return result;
                }
                while (mid > 0 && array[mid - 1].getNodeId().compareTo(parentId) > -1) {
                    --mid;
                }
                for (int i = mid; i < length; i++) {
                    cmp = array[i].getNodeId().computeRelation(parentId);
                    if (cmp > -1) {
                        boolean add = true;
                        if (childOnly) {
                            add = cmp == NodeId.IS_CHILD;
                        } else if (cmp == NodeId.IS_SELF) {
                            add = includeSelf;
                        }
                        if (add) {
                            switch(mode) {
                                case NodeSet.DESCENDANT:
                                    if (Expression.NO_CONTEXT_ID != contextId) {
                                        array[i].deepCopyContext(parent, contextId);
                                    } else {
                                        array[i].copyContext(parent);
                                    }
                                    array[i].addMatches(parent);
                                    result.add(array[i]);
                                    break;
                                case NodeSet.ANCESTOR:
                                    if (Expression.NO_CONTEXT_ID != contextId) {
                                        parent.deepCopyContext(array[i], contextId);
                                    } else {
                                        parent.copyContext(array[i]);
                                    }
                                    parent.addMatches(array[i]);
                                    result.add(parent, 1);
                                    break;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
            return result;
        }
