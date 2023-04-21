    void addNodeInBestPlace(QuartetTree qt, int node_index, double[][] dm) {
        double best_score = Double.MAX_VALUE;
        TreeEdge best_edge = new TreeEdge(null, null);
        QNode cn_qn = new QNode(-1, null, null, null, node_id++);
        QNode cn_qr = new QNode(-1, null, null, null, node_id++);
        int d = (node_count + 2) / 2;
        if (d == 4) populateEdgesList();
        int best_edge_index = -1;
        int alEdges_size = alEdges.size();
        int[][] distances = null;
        for (int i = 0; i < alEdges_size; i++) {
            TreeEdge te = (TreeEdge) alEdges.get(i);
            if (i == 0) {
                partK = -1;
                partTable = new int[dm.length][dm.length];
                sumTable = new int[dm.length][dm.length];
            } else {
                partK = Integer.MAX_VALUE;
            }
            connectNode(qt, te, node_index, cn_qn, cn_qr);
            rootEdge.q1 = qt.root;
            rootEdge.q2 = qt.root.adj[0];
            partitionTree(qt, rootEdge, dm.length);
            count_partK++;
            if (i == 0) {
                distances = computeDistances(d, dm.length);
            }
            for (int j = 0; j < d; j++) {
                int diff = 0;
                for (int k = 0; k < partTable.length; k++) {
                    diff += Math.abs(partTable[labels[d]][k] - partTable[labels[j]][k]);
                }
                if (diff == 0) diff = 2; else diff = diff + 1;
                dm[labels[d]][labels[j]] = diff;
                dm[labels[j]][labels[d]] = dm[labels[d]][labels[j]];
            }
            double score = computeTreeScore(qt, te, d, dm, i, distances);
            if (score < best_score) {
                best_score = score;
                best_edge.q1 = te.q1;
                best_edge.q2 = te.q2;
                best_edge_index = i;
            }
            disconnectNode(cn_qn);
        }
        connectNode(qt, best_edge, node_index, cn_qn, cn_qr);
        alEdges.set(best_edge_index, new TreeEdge(best_edge.q1, cn_qr));
        labelEdge(best_edge.q1, cn_qr);
        alEdges.add(best_edge_index, new TreeEdge(best_edge.q2, cn_qr));
        labelEdge(best_edge.q2, cn_qr);
        alEdges.add(best_edge_index, new TreeEdge(cn_qn, cn_qr));
        labelEdge(cn_qn, cn_qr);
        oldEdge = best_edge;
        qt.root = cn_qr;
        CombinationGenerator cg = new CombinationGenerator(d, 3);
        avg_contribution[d] = (best_score - last_tree_score) / cg.getTotal().doubleValue();
        last_tree_score = best_score;
    }
