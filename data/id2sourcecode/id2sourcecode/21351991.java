    private boolean transferFromCGDGraphToOrig() {
        boolean result = false;
        ArrayList nodes = graph.getNodes();
        HashMap edges = graph.getEdges();
        if (nodes == null) return result;
        if (nodes.size() != rootNumbNodes) {
            System.out.println("nodes.size=" + nodes.size() + " and origNumbNodes=" + rootNumbNodes);
            return result;
        }
        for (int i = 0; i < nodes.size(); i++) {
            CGDNode cgdNode = (CGDNode) nodes.get(i);
            int nIndex = cgdNode.getIndex();
            Object node = null;
            Iterator ic = objectData.entrySet().iterator();
            while (ic.hasNext()) {
                Map.Entry e = (Map.Entry) ic.next();
                CGDNode o = (CGDNode) e.getValue();
                if (o.equals(cgdNode)) {
                    node = e.getKey();
                }
            }
        }
        result = true;
        return result;
    }
