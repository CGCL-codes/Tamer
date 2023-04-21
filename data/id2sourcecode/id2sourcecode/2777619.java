    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int nodeWidth = root.getWidth();
        int nodeHeight = root.getHeight();
        int maxLevelWidth = (maxWidth) * (nodeWidth + interNodeWidth) - interNodeWidth;
        int currX = xMargin + (maxLevelWidth - nodeWidth) / 2;
        int currY = yMargin;
        int levelWidth;
        boolean selected = false;
        if (this.selectedNode != null && this.selectedNode == nodes[0][0]) ((ITreeNodeRenderer) nodes[0][0]).renderNode(g2, currX, currY, ITreeNodeRenderer.SELECTED_NODE); else if (selectedPathNodes.contains(nodes[0][0])) ((ITreeNodeRenderer) nodes[0][0]).renderNode(g2, currX, currY, ITreeNodeRenderer.PATH_NODE); else if (this.selectedNode != null) ((ITreeNodeRenderer) nodes[0][0]).renderNode(g2, currX, currY, ITreeNodeRenderer.NON_PATH_NODE); else ((ITreeNodeRenderer) nodes[0][0]).renderNode(g2, currX, currY, ITreeNodeRenderer.STANDARD_NODE);
        for (int i = 1; i < nodes.length; i++) {
            currY += (nodeHeight + interNodeHeight);
            levelWidth = (nodes[i].length) * (nodeWidth + interNodeWidth) - interNodeWidth;
            currX = xMargin + (maxLevelWidth - levelWidth) / 2;
            for (int j = 0; j < nodes[i].length; j++) {
                if (this.selectedNode != null && this.selectedNode == nodes[i][j]) ((ITreeNodeRenderer) nodes[i][j]).renderNode(g2, currX, currY, ITreeNodeRenderer.SELECTED_NODE); else if (selectedPathNodes.contains(nodes[i][j])) ((ITreeNodeRenderer) nodes[i][j]).renderNode(g2, currX, currY, ITreeNodeRenderer.PATH_NODE); else if (this.selectedNode != null) ((ITreeNodeRenderer) nodes[i][j]).renderNode(g2, currX, currY, ITreeNodeRenderer.NON_PATH_NODE); else ((ITreeNodeRenderer) nodes[i][j]).renderNode(g2, currX, currY, ITreeNodeRenderer.STANDARD_NODE);
                currX += nodeWidth + interNodeWidth;
            }
        }
        renderConnectors(g);
    }
