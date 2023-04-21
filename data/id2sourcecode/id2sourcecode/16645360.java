    private void drawLabel(int fieldX, int fieldY, Graphics boardGraphics, Image boardImage, int boardWidth) {
        setComposite(COMPOSITE_97);
        setFont(m_graphics, m_size);
        FontMetrics fontMetrics = m_graphics.getFontMetrics();
        LineMetrics lineMetrics = fontMetrics.getLineMetrics(m_label, m_graphics);
        int width = fontMetrics.stringWidth(m_label);
        int height = fontMetrics.getHeight();
        int ascent = (int) lineMetrics.getAscent();
        int x = Math.max((m_size - width) / 2, 0);
        int y = (ascent + m_size) / 2;
        if (m_ghostStone == null) {
            if (m_color == BLACK) m_graphics.setColor(Color.white); else m_graphics.setColor(Color.black);
        } else {
            if (m_ghostStone == BLACK) m_graphics.setColor(Color.white); else m_graphics.setColor(Color.black);
        }
        Rectangle oldClip = m_graphics.getClipBounds();
        width = Math.min(width, (int) (0.95 * m_size));
        m_graphics.setClip(x, y - ascent, width, height);
        if (m_color == EMPTY && m_ghostStone == null) {
            Rectangle oldBoardClip = boardGraphics.getClipBounds();
            boardGraphics.setClip(fieldX + x, fieldY + y - ascent, width, height);
            boardGraphics.drawImage(boardImage, 0, 0, boardWidth, boardWidth, null);
            boardGraphics.setClip(oldBoardClip);
            m_graphics.setColor(Color.black);
        }
        m_graphics.drawString(m_label, x, y);
        m_graphics.setClip(oldClip);
    }
