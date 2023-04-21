        private void makeSnapshot() {
            SIZE.setSize(contents.getPreferredSize());
            if (SIZE.width < TinyPopupMenuBorder.SHADOW_SIZE || SIZE.height < TinyPopupMenuBorder.SHADOW_SIZE) return;
            Object co = contents.getClientProperty(COMPONENT_ORIENTATION_KEY);
            boolean isLeftToRight = (co == null ? true : ((ComponentOrientation) co).isLeftToRight());
            if (isLeftToRight) {
                RECT.setBounds(x + SIZE.width - TinyPopupMenuBorder.SHADOW_SIZE, y, 5, SIZE.height);
                vertImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
                contents.putClientProperty(VERTICAL_IMAGE_KEY, vertImg);
                RECT.setBounds(x, y + SIZE.height - TinyPopupMenuBorder.SHADOW_SIZE, SIZE.width, 5);
                horzImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
                contents.putClientProperty(HORIZONTAL_IMAGE_KEY, horzImg);
                JRootPane rootPane = SwingUtilities.getRootPane(owner);
                if (rootPane != null) {
                    JLayeredPane layeredPane = rootPane.getLayeredPane();
                    if (layeredPane != null) {
                        int layeredPaneWidth = layeredPane.getWidth();
                        int layeredPaneHeight = layeredPane.getHeight();
                        POINT.x = x;
                        POINT.y = y;
                        SwingUtilities.convertPointFromScreen(POINT, layeredPane);
                        RECT.x = POINT.x;
                        RECT.y = POINT.y + SIZE.height - TinyPopupMenuBorder.SHADOW_SIZE;
                        RECT.width = SIZE.width;
                        RECT.height = TinyPopupMenuBorder.SHADOW_SIZE;
                        if ((RECT.x + RECT.width) > layeredPaneWidth) {
                            RECT.width = layeredPaneWidth - RECT.x;
                        }
                        if ((RECT.y + RECT.height) > layeredPaneHeight) {
                            RECT.height = layeredPaneHeight - RECT.y;
                        }
                        Graphics g = horzImg.createGraphics();
                        if (!RECT.isEmpty()) {
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
                            g.translate(RECT.x, RECT.y);
                        }
                        Iterator ii = DIALOGS.iterator();
                        while (ii.hasNext()) {
                            Window window = (Window) ii.next();
                            int windowWidth = window.getWidth();
                            int windowHeight = window.getHeight();
                            POINT.x = x;
                            POINT.y = y;
                            SwingUtilities.convertPointFromScreen(POINT, window);
                            RECT.x = POINT.x;
                            RECT.y = POINT.y + SIZE.height - TinyPopupMenuBorder.SHADOW_SIZE;
                            RECT.width = SIZE.width;
                            RECT.height = TinyPopupMenuBorder.SHADOW_SIZE;
                            if ((RECT.x + RECT.width) > windowWidth) {
                                RECT.width = windowWidth - RECT.x;
                            }
                            if ((RECT.y + RECT.height) > windowHeight) {
                                RECT.height = windowHeight - RECT.y;
                            }
                            if (!RECT.isEmpty()) {
                                g.translate(-RECT.x, -RECT.y);
                                g.setClip(RECT);
                                window.paintAll(g);
                                g.translate(RECT.x, RECT.y);
                            }
                        }
                        g.dispose();
                        POINT.x = x;
                        POINT.y = y;
                        SwingUtilities.convertPointFromScreen(POINT, layeredPane);
                        RECT.x = POINT.x + SIZE.width - TinyPopupMenuBorder.SHADOW_SIZE;
                        RECT.y = POINT.y;
                        RECT.width = TinyPopupMenuBorder.SHADOW_SIZE;
                        RECT.height = SIZE.height;
                        if ((RECT.x + RECT.width) > layeredPaneWidth) {
                            RECT.width = layeredPaneWidth - RECT.x;
                        }
                        if ((RECT.y + RECT.height) > layeredPaneHeight) {
                            RECT.height = layeredPaneHeight - RECT.y;
                        }
                        g = vertImg.createGraphics();
                        if (!RECT.isEmpty()) {
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
                            g.translate(RECT.x, RECT.y);
                        }
                        ii = DIALOGS.iterator();
                        while (ii.hasNext()) {
                            Window window = (Window) ii.next();
                            int windowWidth = window.getWidth();
                            int windowHeight = window.getHeight();
                            POINT.x = x;
                            POINT.y = y;
                            SwingUtilities.convertPointFromScreen(POINT, window);
                            RECT.x = POINT.x + SIZE.width - TinyPopupMenuBorder.SHADOW_SIZE;
                            RECT.y = POINT.y;
                            RECT.width = TinyPopupMenuBorder.SHADOW_SIZE;
                            RECT.height = SIZE.height;
                            if ((RECT.x + RECT.width) > windowWidth) {
                                RECT.width = windowWidth - RECT.x;
                            }
                            if ((RECT.y + RECT.height) > windowHeight) {
                                RECT.height = windowHeight - RECT.y;
                            }
                            if (!RECT.isEmpty()) {
                                g.translate(-RECT.x, -RECT.y);
                                g.setClip(RECT);
                                window.paintAll(g);
                                g.translate(RECT.x, RECT.y);
                            }
                        }
                        g.dispose();
                    }
                }
            } else {
                RECT.setBounds(x, y, 5, SIZE.height);
                vertImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
                contents.putClientProperty(VERTICAL_IMAGE_KEY, vertImg);
                RECT.setBounds(x, y + SIZE.height - TinyPopupMenuBorder.SHADOW_SIZE, SIZE.width, 5);
                horzImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
                contents.putClientProperty(HORIZONTAL_IMAGE_KEY, horzImg);
                JRootPane rootPane = SwingUtilities.getRootPane(owner);
                if (rootPane != null) {
                    JLayeredPane layeredPane = rootPane.getLayeredPane();
                    if (layeredPane != null) {
                        int layeredPaneWidth = layeredPane.getWidth();
                        int layeredPaneHeight = layeredPane.getHeight();
                        POINT.x = x;
                        POINT.y = y;
                        SwingUtilities.convertPointFromScreen(POINT, layeredPane);
                        RECT.x = POINT.x;
                        RECT.y = POINT.y + SIZE.height - TinyPopupMenuBorder.SHADOW_SIZE;
                        RECT.width = SIZE.width;
                        RECT.height = TinyPopupMenuBorder.SHADOW_SIZE;
                        if ((RECT.x + RECT.width) > layeredPaneWidth) {
                            RECT.width = layeredPaneWidth - RECT.x;
                        }
                        if ((RECT.y + RECT.height) > layeredPaneHeight) {
                            RECT.height = layeredPaneHeight - RECT.y;
                        }
                        Graphics g = horzImg.createGraphics();
                        if (!RECT.isEmpty()) {
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
                            g.translate(RECT.x, RECT.y);
                        }
                        Iterator ii = DIALOGS.iterator();
                        while (ii.hasNext()) {
                            Window window = (Window) ii.next();
                            int windowWidth = window.getWidth();
                            int windowHeight = window.getHeight();
                            POINT.x = x;
                            POINT.y = y;
                            SwingUtilities.convertPointFromScreen(POINT, window);
                            RECT.x = POINT.x;
                            RECT.y = POINT.y + SIZE.height - TinyPopupMenuBorder.SHADOW_SIZE;
                            RECT.width = SIZE.width;
                            RECT.height = TinyPopupMenuBorder.SHADOW_SIZE;
                            if ((RECT.x + RECT.width) > windowWidth) {
                                RECT.width = windowWidth - RECT.x;
                            }
                            if ((RECT.y + RECT.height) > windowHeight) {
                                RECT.height = windowHeight - RECT.y;
                            }
                            if (!RECT.isEmpty()) {
                                g.translate(-RECT.x, -RECT.y);
                                g.setClip(RECT);
                                window.paintAll(g);
                                g.translate(RECT.x, RECT.y);
                            }
                        }
                        g.dispose();
                        POINT.x = x;
                        POINT.y = y;
                        SwingUtilities.convertPointFromScreen(POINT, layeredPane);
                        RECT.x = POINT.x;
                        RECT.y = POINT.y;
                        RECT.width = TinyPopupMenuBorder.SHADOW_SIZE;
                        RECT.height = SIZE.height;
                        if ((RECT.x + RECT.width) > layeredPaneWidth) {
                            RECT.width = layeredPaneWidth - RECT.x;
                        }
                        if ((RECT.y + RECT.height) > layeredPaneHeight) {
                            RECT.height = layeredPaneHeight - RECT.y;
                        }
                        g = vertImg.createGraphics();
                        if (!RECT.isEmpty()) {
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
                            g.translate(RECT.x, RECT.y);
                        }
                        ii = DIALOGS.iterator();
                        while (ii.hasNext()) {
                            Window window = (Window) ii.next();
                            int windowWidth = window.getWidth();
                            int windowHeight = window.getHeight();
                            POINT.x = x;
                            POINT.y = y;
                            SwingUtilities.convertPointFromScreen(POINT, window);
                            RECT.x = POINT.x;
                            RECT.y = POINT.y;
                            RECT.width = TinyPopupMenuBorder.SHADOW_SIZE;
                            RECT.height = SIZE.height;
                            if ((RECT.x + RECT.width) > windowWidth) {
                                RECT.width = windowWidth - RECT.x;
                            }
                            if ((RECT.y + RECT.height) > windowHeight) {
                                RECT.height = windowHeight - RECT.y;
                            }
                            if (!RECT.isEmpty()) {
                                g.translate(-RECT.x, -RECT.y);
                                g.setClip(RECT);
                                window.paintAll(g);
                                g.translate(RECT.x, RECT.y);
                            }
                        }
                        g.dispose();
                    }
                }
            }
        }
