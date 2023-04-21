    private void drawText(Graphics graphics) {
        GameTreePanel.Label labelMode = m_gameTreePanel.getLabelMode();
        if (labelMode == GameTreePanel.Label.NONE) return;
        Move move = m_node.getMove();
        int size = m_gameTreePanel.getNodeSize();
        String text;
        if (labelMode == GameTreePanel.Label.MOVE) {
            if (move.getPoint() == null) return;
            text = move.getPoint().toString();
        } else text = Integer.toString(m_moveNumber);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        LineMetrics lineMetrics = fontMetrics.getLineMetrics(text, graphics);
        int textWidth = fontMetrics.stringWidth(text);
        int ascent = (int) lineMetrics.getAscent();
        int xText = (size - textWidth) / 2;
        int yText = (ascent + size) / 2;
        if (move.getColor() == BLACK) graphics.setColor(Color.white); else graphics.setColor(Color.black);
        graphics.drawString(text, xText, yText);
    }
