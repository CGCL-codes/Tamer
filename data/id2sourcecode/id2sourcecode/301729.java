    public void calculateCoordinates() {
        createNodeLayers();
        totalTreeWidth = 0;
        totalTreeHeight = (nodeLevels.size() + 1) * treeLevelHeight;
        if (!model.usesTerminals) {
            for (int i = nodeLevels.size() - 1; i >= 0; i--) {
                ArrayList<Integer> nodes = nodeLevels.get(i);
                for (int j = 0; j < nodes.size(); j++) {
                    TreeViewNode node = treeNodes.get(nodes.get(j));
                    if (node.children.size() > 0) {
                        node.subTreeWidth = collectWidths(node.children);
                    } else {
                        node.subTreeWidth = 1;
                    }
                }
            }
            treeNodes.get(rootID).x = treeNodes.get(rootID).subTreeWidth * treeNodesDistance / 2;
            for (int i = 0; i < nodeLevels.size(); i++) {
                ArrayList<Integer> nodes = nodeLevels.get(i);
                int xOffset = 100;
                int parent = -1;
                for (int j = 0; j < nodes.size(); j++) {
                    int subtreeWidth = treeNodes.get(nodes.get(j)).subTreeWidth * treeNodesDistance;
                    xOffset += subtreeWidth;
                    if (i > 0 && treeNodes.get(nodes.get(j)).parent != parent) {
                        parent = treeNodes.get(nodes.get(j)).parent;
                        xOffset = (int) (treeNodes.get(parent).x + treeNodes.get(parent).subTreeWidth * ((double) (treeNodes.get(nodes.get(j)).subTreeWidth) / treeNodes.get(parent).subTreeWidth - 0.5) * treeNodesDistance);
                    }
                    if (i > 0) {
                        treeNodes.get(nodes.get(j)).x = xOffset - subtreeWidth / 2;
                    }
                }
                if (nodes.size() > 0 && treeNodes.get(nodes.get(nodes.size() - 1)).x + treeNodesDistance > totalTreeWidth) {
                    totalTreeWidth = treeNodes.get(nodes.get(nodes.size() - 1)).x + treeNodesDistance;
                }
            }
        } else {
            ArrayList<Integer> terminals = nodeLevels.get(0);
            int xpos = 100;
            for (int t : terminals) {
                treeNodes.get(t).y = nodeLevels.size() * treeLevelHeight;
                treeNodes.get(t).x = xpos;
                xpos += treeNodesDistance;
            }
            totalTreeWidth = terminals.size() * (treeNodesDistance + 2);
            for (int j = nodeLevels.size() - 1; j > 0; j--) {
                for (int n : nodeLevels.get(j)) {
                    int minX = 1000;
                    int maxX = 0;
                    for (int c : treeNodes.get(n).children) {
                        int newX = treeNodes.get(c).x;
                        if (newX < minX) minX = newX;
                        if (newX > maxX) maxX = newX;
                    }
                    treeNodes.get(n).x = (minX + maxX) / 2;
                }
            }
            if (model.usesPreTerminals) {
                for (int t : terminals) {
                    treeNodes.get(treeNodes.get(t).parent).y = treeNodes.get(t).y - treeLevelHeight;
                }
            }
        }
    }
