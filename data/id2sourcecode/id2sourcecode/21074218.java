    public void calculateCoordinates() {
        createNodeLayers();
        setTotalTreeWidth(0);
        setTotalTreeHeight((int) ((getNodeLevels().size() + 2) * treeLevelHeight * zoomFactor));
        if (!usesTerminals) {
            for (int i = getNodeLevels().size() - 1; i >= 0; i--) {
                ArrayList<Integer> nodes = getNodeLevels().get(i);
                for (int j = 0; j < nodes.size(); j++) {
                    TreeViewNode node = treeNodes.get(nodes.get(j));
                    ArrayList<Integer> children = getVisibleVirtualChildren(nodes.get(j));
                    node.subTreeWidth = constructWidthVector(children);
                }
            }
            treeNodes.get(rootID).x = (int) (treeNodes.get(rootID).subTreeWidth.maximumLeftDistance() * treeNodesDistance / 2 * zoomFactor);
            for (int i = 0; i < getNodeLevels().size(); i++) {
                ArrayList<Integer> nodes = getNodeLevels().get(i);
                int xOffset = 0;
                if (nodes.size() > 0) xOffset = (int) (treeNodes.get(nodes.get(0)).subTreeWidth.maximumLeftDistance() * treeNodesDistance / 2.0 * zoomFactor);
                int parent = -1;
                WidthVector subtreeWidth = new WidthVector();
                int numNodes = nodes.size();
                WidthVector lastSubtreeWidth;
                for (int j = 0; j < numNodes; j++) {
                    lastSubtreeWidth = subtreeWidth;
                    subtreeWidth = treeNodes.get(nodes.get(j)).subTreeWidth;
                    xOffset += WidthVector.computeNecessaryDistance(lastSubtreeWidth, subtreeWidth) * treeNodesDistance / 2 * zoomFactor;
                    int newParent = getVisibleParent(nodes.get(j));
                    if (i > 0 && newParent != parent) {
                        parent = newParent;
                        xOffset = (int) ((treeNodes.get(parent).x - (treeNodes.get(parent).subTreeWidth.start.get(1) * 0.5 - 0.5) * treeNodesDistance));
                    }
                    if (i > 0) {
                        treeNodes.get(nodes.get(j)).x = xOffset;
                    }
                    treeNodes.get(nodes.get(j)).y = (int) (treeLevelHeight * zoomFactor) * i + 50;
                }
                if (nodes.size() > 0 && treeNodes.get(nodes.get(nodes.size() - 1)).x + (int) (treeNodesDistance * zoomFactor) > getTotalTreeWidth()) {
                    setTotalTreeWidth(treeNodes.get(nodes.get(nodes.size() - 1)).x + (int) (treeNodesDistance * zoomFactor));
                }
            }
        } else {
            ArrayList<Integer> terminals = getNodeLevels().get(0);
            int xpos = (int) (100 * zoomFactor);
            for (int t : terminals) {
                treeNodes.get(t).y = (int) (getNodeLevels().size() * treeLevelHeight * zoomFactor);
                treeNodes.get(t).x = xpos;
                xpos += treeNodesDistance * zoomFactor;
            }
            setTotalTreeWidth(terminals.size() * (int) ((treeNodesDistance + 2) * zoomFactor));
            for (int j = getNodeLevels().size() - 1; j > 0; j--) {
                for (int n : getNodeLevels().get(j)) {
                    int minX = Integer.MAX_VALUE;
                    int maxX = 0;
                    for (int c : treeNodes.get(n).children) {
                        int newX = treeNodes.get(c).x;
                        if (newX < minX) minX = newX;
                        if (newX > maxX) maxX = newX;
                    }
                    treeNodes.get(n).y = 50 + (int) ((j - 1) * treeLevelHeight * zoomFactor);
                    treeNodes.get(n).x = (minX + maxX) / 2;
                }
            }
            if (model != null && model.usesPreTerminals) {
                for (int t : terminals) {
                    treeNodes.get(treeNodes.get(t).getParent()).y = treeNodes.get(t).y - (int) (treeLevelHeight * zoomFactor);
                }
            }
        }
    }
