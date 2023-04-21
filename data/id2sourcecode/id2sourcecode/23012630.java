    private void drawBackground() {
        m_graphics2D.setColor(Color.decode("#e0e0e0"));
        m_graphics2D.fillRect(0, 0, m_imgWidth, m_imgHeight);
        m_graphics2D.setColor(Color.WHITE);
        m_graphics2D.fillRect(m_left, m_top, m_width, m_height);
        m_graphics2D.setColor(Color.BLACK);
        if (m_title != null) {
            int width = m_metrics.stringWidth(m_title) + 10;
            int height = (int) (m_fontHeight * 1.4);
            int x = m_left + (m_width - width) / 2;
            int y = (m_top - height) / 2;
            m_graphics2D.setColor(Color.decode("#ffffe1"));
            m_graphics2D.fillRect(x, y, width, height);
            m_graphics2D.setColor(Color.DARK_GRAY);
            m_graphics2D.drawRect(x, y, width, height);
            drawString(m_title, m_left + m_width / 2, m_top / 2);
        }
    }
