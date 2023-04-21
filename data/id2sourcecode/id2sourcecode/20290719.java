    private void snapshot() {
        try {
            Dimension size = heavyWeightContainer.getPreferredSize();
            int width = size.width;
            int height = size.height;
            if ((width <= 0) || (height <= SHADOW_SIZE)) {
                return;
            }
            Robot robot = new Robot();
            RECT.setBounds(x, y + height - SHADOW_SIZE, width, SHADOW_SIZE);
            BufferedImage hShadowBg = robot.createScreenCapture(RECT);
            RECT.setBounds(x + width - SHADOW_SIZE, y, SHADOW_SIZE, height - SHADOW_SIZE);
            BufferedImage vShadowBg = robot.createScreenCapture(RECT);
            JComponent parent = (JComponent) contents.getParent();
            parent.putClientProperty(ShadowPopupFactory.PROP_HORIZONTAL_BACKGROUND, hShadowBg);
            parent.putClientProperty(ShadowPopupFactory.PROP_VERTICAL_BACKGROUND, vShadowBg);
            Container layeredPane = getLayeredPane();
            if (layeredPane == null) {
                return;
            }
            int layeredPaneWidth = layeredPane.getWidth();
            int layeredPaneHeight = layeredPane.getHeight();
            POINT.x = x;
            POINT.y = y;
            SwingUtilities.convertPointFromScreen(POINT, layeredPane);
            RECT.x = POINT.x;
            RECT.y = POINT.y + height - SHADOW_SIZE;
            RECT.width = width;
            RECT.height = SHADOW_SIZE;
            if ((RECT.x + RECT.width) > layeredPaneWidth) {
                RECT.width = layeredPaneWidth - RECT.x;
            }
            if ((RECT.y + RECT.height) > layeredPaneHeight) {
                RECT.height = layeredPaneHeight - RECT.y;
            }
            if (!RECT.isEmpty()) {
                Graphics g = hShadowBg.createGraphics();
                g.translate(-RECT.x, -RECT.y);
                g.setClip(RECT);
                if (layeredPane instanceof JComponent) {
                    JComponent c = (JComponent) layeredPane;
                    boolean doubleBuffered = c.isDoubleBuffered();
                    c.setDoubleBuffered(false);
                    c.paintAll(g);
                    c.setDoubleBuffered(doubleBuffered);
                } else {
                    layeredPane.paintAll(g);
                }
                g.dispose();
            }
            RECT.x = POINT.x + width - SHADOW_SIZE;
            RECT.y = POINT.y;
            RECT.width = SHADOW_SIZE;
            RECT.height = height - SHADOW_SIZE;
            if ((RECT.x + RECT.width) > layeredPaneWidth) {
                RECT.width = layeredPaneWidth - RECT.x;
            }
            if ((RECT.y + RECT.height) > layeredPaneHeight) {
                RECT.height = layeredPaneHeight - RECT.y;
            }
            if (!RECT.isEmpty()) {
                Graphics g = vShadowBg.createGraphics();
                g.translate(-RECT.x, -RECT.y);
                g.setClip(RECT);
                if (layeredPane instanceof JComponent) {
                    JComponent c = (JComponent) layeredPane;
                    boolean doubleBuffered = c.isDoubleBuffered();
                    c.setDoubleBuffered(false);
                    c.paintAll(g);
                    c.setDoubleBuffered(doubleBuffered);
                } else {
                    layeredPane.paintAll(g);
                }
                g.dispose();
            }
        } catch (AWTException e) {
            canSnapshot = false;
        } catch (SecurityException e) {
            canSnapshot = false;
        }
    }
