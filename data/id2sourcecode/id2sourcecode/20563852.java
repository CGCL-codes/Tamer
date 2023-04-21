    protected BowEdge getBowEdge(int flow) {
        if (flow > maxCapacity || flow < 0) return null;
        int l = 0;
        int r = bowEdges.size() - 1;
        int m = (l + r) / 2;
        BowEdge bEdge;
        do {
            bEdge = bowEdges.get(m);
            if (flow >= bEdge.minFlowToUseThisEdge && flow <= bEdge.maxFlowToUseThisEdge) return bEdge; else if (flow < bEdge.minFlowToUseThisEdge) r = m - 1; else if (flow > bEdge.maxFlowToUseThisEdge) l = m + 1;
            m = (l + r) / 2;
        } while (l - r >= 0);
        return null;
    }
