    public void optimizeLayout(final HGraph pGraph, final LOrder pOrder) {
        double lTMax, lTMin, lTGlobal, lTNew, lDeltaT;
        double lZNew, lZOld, lTthresh;
        double lFRub, lD;
        int lActRound, lMaxRound;
        lMaxRound = 5;
        lDeltaT = 0.05;
        lTMax = 1;
        lTMin = 0;
        lTthresh = lTMin + (lTMax - lTMin) / 100 * 5;
        final Map<HNode, Double> lTMap = new HashMap<HNode, Double>();
        final Map<HNode, Double> lIMap = new HashMap<HNode, Double>();
        for (int i = 1; i <= pOrder.countLayer(); ++i) {
            for (int j = 1; j <= pOrder.countLayerSize(i); ++j) {
                final HNode lCurNode = pOrder.getNode(i, j);
                lTMap.put(lCurNode, (lTMax + lTMin) / 2.0);
                lIMap.put(lCurNode, 0.0);
            }
        }
        lTGlobal = (lTMax + lTMin) / 2;
        lZNew = this.getZValue(pGraph);
        lZOld = Double.POSITIVE_INFINITY;
        lActRound = 0;
        while ((lActRound < lMaxRound) && ((lZNew < lZOld) || (lTGlobal >= lTthresh))) {
            lZOld = lZNew;
            for (int i = 1; i <= pOrder.countLayer(); ++i) {
                for (int j = 1; j <= pOrder.countLayerSize(i); ++j) {
                    final HNode lCurNode = pOrder.getNode(i, j);
                    lD = lFRub = this.getFRub(lCurNode, pGraph);
                    if (lFRub < 0) {
                        if (j > 1) {
                            final HNode lLeftNode = pOrder.getNode(i, j - 1);
                            double lLeftX;
                            if (lLeftNode instanceof DummyHNode) {
                                lLeftX = ((DummyHNode) lLeftNode).getLocation(pGraph).getX();
                            } else {
                                lLeftX = lLeftNode.getModel().getPosition(pGraph.getModel()).getX();
                            }
                            double lCurX;
                            if (lCurNode instanceof DummyHNode) {
                                lCurX = ((DummyHNode) lCurNode).getLocation(pGraph).getX();
                            } else {
                                lCurX = lCurNode.getModel().getPosition(pGraph.getModel()).getX();
                            }
                            final double lMax = lLeftX + lLeftNode.getModelDimension(pGraph.getModel()).getWidth() + this.fHOffset - lCurX;
                            if (lMax > lFRub) {
                                lD = lMax;
                            }
                        }
                    } else {
                        if (j < pOrder.countLayerSize(i)) {
                            final HNode lRightNode = pOrder.getNode(i, j + 1);
                            double lRightX;
                            if (lRightNode instanceof DummyHNode) {
                                lRightX = ((DummyHNode) lRightNode).getLocation(pGraph).getX();
                            } else {
                                lRightX = lRightNode.getModel().getPosition(pGraph.getModel()).getX();
                            }
                            double lCurX;
                            if (lCurNode instanceof DummyHNode) {
                                lCurX = ((DummyHNode) lCurNode).getLocation(pGraph).getX();
                            } else {
                                lCurX = lCurNode.getModel().getPosition(pGraph.getModel()).getX();
                            }
                            final double lMin = lRightX - lCurX - lCurNode.getModelDimension(pGraph.getModel()).getWidth() - this.fHOffset;
                            if (lMin < lFRub) {
                                lD = lMin;
                            }
                        }
                    }
                    if ((lD == 0) || (lD * lIMap.get(lCurNode).doubleValue() < 0)) {
                        lTNew = lTMap.get(lCurNode).doubleValue() - lDeltaT;
                        if (lTNew < lTMin) {
                            lTNew = lTMin;
                        }
                    } else {
                        lTNew = lTMap.get(lCurNode).doubleValue() + lDeltaT;
                        if (lTNew > lTMax) {
                            lTNew = lTMax;
                        }
                    }
                    lTGlobal += lTNew - lTMap.get(lCurNode) / pGraph.getHNodes().size();
                    if (lCurNode instanceof DummyHNode) {
                        final DummyHNode lCurNodeD = (DummyHNode) lCurNode;
                        lCurNodeD.setLocation(pGraph, new Point2D.Double(lCurNodeD.getLocation(pGraph).getX() + lD, lCurNodeD.getLocation(pGraph).getY()));
                    } else {
                        lCurNode.setLocation(pGraph.getModel(), new Point2D.Double(lCurNode.getModel().getPosition(pGraph.getModel()).getX() + lD, lCurNode.getModel().getPosition(pGraph.getModel()).getY()));
                    }
                    lIMap.put(lCurNode, new Double(lD));
                    lTMap.put(lCurNode, new Double(lTNew));
                }
            }
            lZNew = this.getZValue(pGraph);
            ++lActRound;
        }
    }
