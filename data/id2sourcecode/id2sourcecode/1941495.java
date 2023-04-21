    protected List<Difference> getChanges(Diff.change script, DocumentImpl docA, DocumentImpl docB, DiffNode[] nodesA, DiffNode[] nodesB) throws XMLStreamException {
        List<Difference> changes = new ArrayList<Difference>();
        Map<NodeId, Difference> inserts = new TreeMap<NodeId, Difference>();
        Diff.change next = script;
        while (next != null) {
            int start0 = next.line0;
            int start = next.line1;
            int last = start + next.inserted;
            int lastDeleted = start0 + next.deleted;
            if (next.inserted > 0) {
                if (next.deleted == 0) {
                    int offsetFix = 0;
                    for (int i = last - 1; i > start; i--) {
                        DiffNode node = nodesB[i];
                        if (node.nodeType == XMLStreamReader.START_ELEMENT && start - (last - i) > 0) {
                            DiffNode before = nodesB[start - (last - i)];
                            if (before.nodeType == XMLStreamReader.START_ELEMENT && before.qname.equals(node.qname)) offsetFix++;
                        } else break;
                    }
                    if (offsetFix > 0) {
                        start = start - offsetFix;
                        start0 = start0 - offsetFix;
                        last = start + next.inserted;
                    }
                }
                Difference.Insert diff;
                if (nodesA[start0].nodeType == XMLStreamReader.END_ELEMENT) {
                    diff = new Difference.Append(new NodeProxy(docA, nodesA[start0].nodeId), docB);
                    changes.add(diff);
                } else {
                    diff = (Difference.Insert) inserts.get(nodesA[start0].nodeId);
                    if (diff == null) {
                        diff = new Difference.Insert(new NodeProxy(docA, nodesA[start0].nodeId), docB);
                        inserts.put(nodesA[start0].nodeId, diff);
                    }
                }
                DiffNode[] nodes = new DiffNode[last - start];
                int j = 0;
                for (int i = start; i < last; i++, j++) {
                    if (LOG.isTraceEnabled()) LOG.trace(Integer.toString(i) + " " + nodesB[i]);
                    nodes[j] = nodesB[i];
                }
                diff.addNodes(nodes);
            }
            if (next.deleted > 0) {
                DiffNode beforeElement = nodesA[start0 - 1];
                DiffNode lastElement = nodesA[lastDeleted - 1];
                if (beforeElement.qname != null && lastElement.qname != null && beforeElement.qname.equals(lastElement.qname) && beforeElement.nodeType == XMLStreamReader.START_ELEMENT && lastElement.nodeType == XMLStreamReader.START_ELEMENT) {
                    start0--;
                    lastDeleted--;
                }
                if (LOG.isTraceEnabled()) LOG.trace("Deleted: " + start0 + " last: " + lastDeleted);
                for (int i = start0; i < lastDeleted; i++) {
                    boolean elementDeleted = false;
                    if (nodesA[i].nodeType == XMLStreamReader.START_ELEMENT) {
                        for (int j = i; j < lastDeleted; j++) {
                            if (nodesA[j].nodeType == XMLStreamReader.END_ELEMENT && nodesA[j].nodeId.equals(nodesA[i].nodeId)) {
                                Difference.Delete diff = new Difference.Delete(new NodeProxy(docA, nodesA[i].nodeId));
                                changes.add(diff);
                                i = j;
                                elementDeleted = true;
                                break;
                            }
                        }
                    }
                    if (!elementDeleted) {
                        Difference.Delete diff = new Difference.Delete(nodesA[i].nodeType, new NodeProxy(docA, nodesA[i].nodeId));
                        changes.add(diff);
                    }
                }
            }
            next = next.link;
        }
        for (Difference diff : inserts.values()) {
            changes.add(diff);
        }
        return changes;
    }
