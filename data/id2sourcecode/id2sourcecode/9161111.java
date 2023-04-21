    void addNodeInBestPlace(QuartetTree qt, int node_index, double[][] dm) {
        double best_score = Double.MAX_VALUE;
        TreeEdge best_edge = new TreeEdge(null, null);
        QNode cn_qn = new QNode(-1, null, null, null, node_id++);
        QNode cn_qr = new QNode(-1, null, null, null, node_id++);
        int d = (node_count + 2) / 2;
        if (d == 4) populateEdgesList();
        int best_edge_index = -1;
        int alEdges_size = alEdges.size();
        for (int i = 0; i < alEdges_size; i++) {
            TreeEdge te = (TreeEdge) alEdges.get(i);
            if (i == 0) {
                partK = -1;
                initPartTables(dm.length);
            } else {
                partK = Integer.MAX_VALUE;
            }
            connectNode(qt, te, node_index, cn_qn, cn_qr);
            rootEdge.q1 = qt.root;
            rootEdge.q2 = qt.root.adj[0];
            partitionTree(qt, rootEdge, dm.length);
            count_partK++;
            double score = computeTreeScore(qt, te, d, dm, i);
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
        last_tree_score = best_score;
    }
