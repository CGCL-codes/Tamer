    protected void calculateIsParalog(Map<BaseTreeNode, Set<String>> innerSets, Set<String> parentOuterSet) {
        Set<String> outerSet = new HashSet<String>(parentOuterSet);
        if (!isRoot()) for (BaseTreeNode sibling : parent.children) {
            if (this != sibling) {
                Set<String> siblingInnerSet = innerSets.get(sibling);
                outerSet.addAll(siblingInnerSet);
            }
        }
        length = -1;
        if (!isTerminal()) {
            for (BaseTreeNode ch : children) {
                PTreeNode child = (PTreeNode) ch;
                child.calculateIsParalog(innerSets, outerSet);
            }
            int size = children.size();
            nodeType = NodeType.unknown;
            for (int i = 0; i < size; i++) {
                PTreeNode child1 = (PTreeNode) children.get(i);
                Set<String> set1 = innerSets.get(child1);
                if (set1 == null) continue;
                for (int j = i + 1; j < size; j++) {
                    PTreeNode child2 = (PTreeNode) children.get(j);
                    Set<String> set2 = innerSets.get(child2);
                    Set<String> test = new HashSet<String>(set1);
                    test.retainAll(set2);
                    if (test.isEmpty()) {
                        if (nodeType == NodeType.unknown) nodeType = NodeType.speciation; else nodeType = NodeType.both;
                    } else {
                        if (nodeType == NodeType.unknown) nodeType = NodeType.duplication; else nodeType = NodeType.both;
                    }
                }
            }
        }
        matchSpecies(innerSets);
        if (!isTerminal()) {
            for (BaseTreeNode ch : children) {
                PTreeNode child = (PTreeNode) ch;
                double maxlen = maxage - child.minage;
                double minlen = minage - child.maxage;
                child.length = (maxlen + minlen) / 2;
            }
        }
        Set<String> innerSet = innerSets.get(this);
        outerSet.retainAll(innerSet);
        isParalog = !outerSet.isEmpty();
    }
