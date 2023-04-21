    public void paint(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (threadIsRecomputing) return;
        g.drawImage(previewImage, 4, 12 + 12, this);
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString(node.getChannel().getName(), 2, 12 + 8);
        g.setColor(Color.white);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        Vector<ConnectionPoint> allCPs = node.getAllConnectionPointsVector();
        for (int i = 0; i < allCPs.size(); i++) {
        }
        g.setColor(Color.yellow);
        g.drawString("?", helpX + 6, helpY + 12);
        if (node.getChannel().isMarkedForExport()) {
            g.drawString("E", 4, helpY + 10);
        }
    }
