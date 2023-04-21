    public void setVisible(boolean i_bIsVisible) {
        if (i_bIsVisible) {
            if (m_bUseBackBuffer) {
                try {
                    Window oWindow = SwingUtilities.getWindowAncestor(this);
                    Insets oInsets = oWindow.getInsets();
                    Rectangle oRectangle = new Rectangle(oWindow.getBounds());
                    oRectangle.x += oInsets.left;
                    oRectangle.y += oInsets.top;
                    oRectangle.width -= oInsets.left + oInsets.right;
                    oRectangle.height -= oInsets.top + oInsets.bottom;
                    m_oImageBuf = new Robot().createScreenCapture(oRectangle);
                    Graphics2D oGraphics = m_oImageBuf.createGraphics();
                    oGraphics.setColor(new Color(255, 255, 255, 160));
                    oGraphics.fillRect(0, 0, m_oImageBuf.getWidth(), m_oImageBuf.getHeight());
                    oGraphics.dispose();
                    oWindow.addComponentListener(m_oComponentAdapter);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
            addMouseListener(m_oMouseAdapter);
            addMouseMotionListener(m_oMouseMotionAdapter);
            addKeyListener(m_oKeyAdapter);
            m_oTimer.start();
        } else {
            m_oTimer.stop();
            m_oImageBuf = null;
            removeMouseListener(m_oMouseAdapter);
            removeMouseMotionListener(m_oMouseMotionAdapter);
            removeKeyListener(m_oKeyAdapter);
            Window oWindow = SwingUtilities.getWindowAncestor(this);
            if (oWindow != null) oWindow.removeComponentListener(m_oComponentAdapter);
        }
        super.setVisible(i_bIsVisible);
    }
