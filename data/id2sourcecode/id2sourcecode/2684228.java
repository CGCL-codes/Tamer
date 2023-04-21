    private void snapshot() {
        try {
            Robot robot = new Robot();
            Dimension size = heavyWeightContainer.getPreferredSize();
            int width = size.width;
            int height = size.height;
            rect.setBounds(x, y + height - SHADOW_SIZE, width, SHADOW_SIZE);
            BufferedImage hShadowBg = robot.createScreenCapture(rect);
            rect.setBounds(x + width - SHADOW_SIZE, y, SHADOW_SIZE, height - SHADOW_SIZE);
            BufferedImage vShadowBg = robot.createScreenCapture(rect);
            JComponent parent = (JComponent) contents.getParent();
            parent.putClientProperty(ShadowPopupFactory.PROP_HORIZONTAL_BACKGROUND, hShadowBg);
            parent.putClientProperty(ShadowPopupFactory.PROP_VERTICAL_BACKGROUND, vShadowBg);
            Container layeredPane = getLayeredPane();
            if (layeredPane == null) {
                return;
            }
            int layeredPaneWidth = layeredPane.getWidth();
            int layeredPaneHeight = layeredPane.getHeight();
            point.x = x;
            point.y = y;
            SwingUtilities.convertPointFromScreen(point, layeredPane);
            rect.x = point.x;
            rect.y = point.y + height - SHADOW_SIZE;
            rect.width = width;
            rect.height = SHADOW_SIZE;
            if ((rect.x + rect.width) > layeredPaneWidth) {
                rect.width = layeredPaneWidth - rect.x;
            }
            if ((rect.y + rect.height) > layeredPaneHeight) {
                rect.height = layeredPaneHeight - rect.y;
            }
            if (!rect.isEmpty()) {
                Graphics g = hShadowBg.createGraphics();
                g.translate(-rect.x, -rect.y);
                g.setClip(rect);
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
            rect.x = point.x + width - SHADOW_SIZE;
            rect.y = point.y;
            rect.width = SHADOW_SIZE;
            rect.height = height - SHADOW_SIZE;
            if ((rect.x + rect.width) > layeredPaneWidth) {
                rect.width = layeredPaneWidth - rect.x;
            }
            if ((rect.y + rect.height) > layeredPaneHeight) {
                rect.height = layeredPaneHeight - rect.y;
            }
            if (!rect.isEmpty()) {
                Graphics g = vShadowBg.createGraphics();
                g.translate(-rect.x, -rect.y);
                g.setClip(rect);
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
