    public void drawNode(Graphics2D g, TextureGraphNode node) {
        final int roundRad = 16;
        if (node.userData == null) node.userData = new NodePreviewImage(node);
        if (node.getChannel().vizType == ChannelVizType.SLOW) g.setColor(col_NodeSlowBG); else g.setColor(col_NodeBG);
        int x = node.getX() + desktopX;
        int y = node.getY() + desktopY;
        int w = TextureGraphNode.width;
        int h = TextureGraphNode.height;
        g.fillRoundRect(x, y, w, h, roundRad, roundRad);
        g.drawImage(((NodePreviewImage) node.userData).previewImage, x + 4, y + 12 + 12, this);
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString(node.getChannel().getName(), x + 2, y + 12 + 8);
        g.setColor(col_NodeBorder);
        g.drawRoundRect(x, y, w, h, roundRad, roundRad);
        for (ConnectionPoint p : node.getAllConnectionPointsVector()) {
            drawConnectionPoint(g, x, y, p);
        }
        g.setColor(Color.yellow);
        g.drawString("?", x + helpX + 6, y + helpY + 12);
        if (node.getChannel().isMarkedForExport()) {
            g.drawString("E", x + 4, y + helpY + 10);
        }
    }
