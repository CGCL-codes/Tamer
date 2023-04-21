    public static <NodeType, EdgeType> GastonTree<NodeType, EdgeType> create(final GastonPath<NodeType, EdgeType> backbone, Leg<NodeType, EdgeType> ack, final Collection<Leg<NodeType, EdgeType>> siblings) {
        final HPGraph<NodeType, EdgeType> path = backbone.toHPFragment().toHPGraph();
        final LocalEnvironment<NodeType, EdgeType> env = LocalEnvironment.env(backbone);
        final int nodeA = ack.getNodeA();
        final int nodeLabel = ack.ref.getToLabel();
        final int edgeLabel = ack.ref.getEdgeLabel();
        int ln = backbone.frontNode;
        int rn = backbone.backNode;
        int le = path.getNodeEdge(ln, 0);
        int re = path.getNodeEdge(rn, 0);
        {
            final int iln = path.getOtherNode(le, ln), irn = path.getOtherNode(re, rn);
            final int lel = path.getEdgeLabelIndex(le, env), rel = path.getEdgeLabelIndex(re, env);
            final int lnl = path.getNodeLabelIndex(ln, env), rnl = path.getNodeLabelIndex(rn, env);
            if ((nodeA == iln && (lel < edgeLabel || (lel == edgeLabel && lnl < nodeLabel))) || (nodeA == irn && (rel < edgeLabel || (rel == edgeLabel && rnl < nodeLabel)))) {
                return null;
            }
        }
        final int length = path.getMaxNodeIndex();
        final DepthRefinement nodes[] = new DepthRefinement[length];
        final DepthRefinement right[] = new DepthRefinement[length];
        final DepthRefinement bb[] = new DepthRefinement[length];
        final DepthRefinement rmp[] = new DepthRefinement[length];
        final int leftArrayLength = path.getNodeCount() / 2;
        final int lastRigthDepth = (path.getNodeCount() - 1) / 2;
        final int rpmNodes[] = new int[length];
        final BitSet rmpEqualsbb = new BitSet(length);
        int sym = 0;
        int tmp;
        boolean rightExtension = false;
        DepthRefinement lend, rend, lack, rack, tmpr;
        nodes[ln] = lack = lend = new DepthRefinement(ln, path.getEdgeLabelIndex(le, env), path.getNodeLabelIndex(ln, env), rpmNodes);
        nodes[rn] = rack = rend = new DepthRefinement(rn, path.getEdgeLabelIndex(re, env), path.getNodeLabelIndex(rn, env), rpmNodes);
        tmp = lack.compareLabels(rack);
        if (tmp != 0) {
            sym = tmp;
        }
        while (re != le) {
            ln = path.getOtherNode(le, ln);
            rn = path.getOtherNode(re, rn);
            if (rn == ln) {
                break;
            }
            for (int i = 0, oldEdge = le; le == oldEdge; i++) {
                le = path.getNodeEdge(ln, i);
            }
            for (int i = 0, oldEdge = re; re == oldEdge; i++) {
                re = path.getNodeEdge(rn, i);
            }
            lack.prev = nodes[ln] = tmpr = new DepthRefinement(ln, path.getEdgeLabelIndex(le, env), path.getNodeLabelIndex(ln, env), rpmNodes);
            lack = tmpr;
            rack.prev = nodes[rn] = tmpr = new DepthRefinement(rn, path.getEdgeLabelIndex(re, env), path.getNodeLabelIndex(rn, env), rpmNodes);
            rack = tmpr;
            tmp = lack.compareLabels(rack);
            if (tmp != 0) {
                sym = tmp;
            }
            rightExtension |= rn == nodeA;
        }
        if (sym == 0 && rightExtension) {
            return null;
        }
        if (sym < 0) {
            tmpr = lack;
            lack = rack;
            rack = tmpr;
            tmpr = lend;
            lend = rend;
            rend = tmpr;
        }
        if (rn == ln) {
            rack.prev = nodes[rn] = tmpr = new DepthRefinement(rn, path.getEdgeLabelIndex(le, env), path.getNodeLabelIndex(rn, env), rpmNodes);
            rack = tmpr;
        }
        lack.prev = rend;
        tmpr = lend;
        int i = 0;
        for (i = length - 1; tmpr != null; i--) {
            rpmNodes[i] = tmpr.nodeA;
            tmpr.nodeA = i;
            rmp[i] = bb[i] = tmpr;
            tmpr = tmpr.prev;
        }
        final Collection<Leg<NodeType, EdgeType>> newSiblings = new ArrayList<Leg<NodeType, EdgeType>>();
        for (final Leg<NodeType, EdgeType> cur : siblings) {
            if (cur.ref.isCycleRefinement()) {
                newSiblings.add(cur);
            } else {
                final int cdepth = nodes[cur.getNodeA()].getDepth() + 1;
                final DepthRefinement ref = new DepthRefinement(cdepth, cur.ref.getEdgeLabel(), cur.ref.getToLabel(), rpmNodes);
                if (cdepth != length && cdepth != lastRigthDepth + 1 && (cdepth != length - 1 || bb[length - 1].compareTo(ref) >= 0) && (cdepth != lastRigthDepth || bb[lastRigthDepth].compareTo(ref) >= 0)) {
                    final Leg<NodeType, EdgeType> nl = new Leg<NodeType, EdgeType>(ref, cur.frag);
                    newSiblings.add(nl);
                    if (cur == ack) {
                        ack = nl;
                    }
                }
            }
        }
        final int ackDepth = ack.ref.getDepth();
        rpmNodes[ackDepth] = ack.frag.correspondingNode;
        DepthRefinement npn = null, pnpn = null, splittNode = null;
        final DepthRefinement ackr = (DepthRefinement) ack.ref;
        int maxDepth;
        if (ackDepth > lastRigthDepth) {
            if (sym == 0) {
                pnpn = ackr;
            }
            ackr.prev = lend;
            lend = ackr;
            maxDepth = lastRigthDepth + leftArrayLength;
        } else {
            ackr.prev = rend;
            rend = ackr;
            maxDepth = lastRigthDepth;
        }
        tmp = bb[ackDepth].compareTo(ackr);
        splittNode = ackr;
        if (tmp == 0) {
            npn = next(bb[ackDepth], ackr);
        } else if (tmp < 0) {
            maxDepth--;
        }
        for (i = 0; i < ackDepth - 1; i++) {
            if (i != lastRigthDepth) {
                right[i] = rmp[i + 1];
            }
        }
        rmp[ackDepth] = right[ackDepth - 1] = (DepthRefinement) ack.ref;
        rmpEqualsbb.set(0, ackDepth - 1);
        rmpEqualsbb.set(ackDepth, bb[ackDepth].compareTo(ackr) == 0);
        lack.prev = null;
        return new GastonTree<NodeType, EdgeType>(backbone.getLevel() + 1, ack, newSiblings, bb, rmp, npn, pnpn, splittNode, lend, rend, leftArrayLength, lastRigthDepth, maxDepth, rpmNodes, right, rmpEqualsbb, backbone.getThreadNumber());
    }
