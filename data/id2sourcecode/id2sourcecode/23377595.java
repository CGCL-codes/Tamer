        private boolean snapshot() {
            try {
                final Dimension size = heavyWeightContainer.getPreferredSize();
                final int width = size.width;
                final int height = size.height;
                if (width <= 0 || height <= SHADOW_SIZE) {
                    return false;
                }
                final Robot robot = new Robot();
                RECT.setBounds(x, y + height - SHADOW_SIZE, width, SHADOW_SIZE);
                final BufferedImage hShadowBg = robot.createScreenCapture(RECT);
                RECT.setBounds(x + width - SHADOW_SIZE, y, SHADOW_SIZE, height - SHADOW_SIZE);
                final BufferedImage vShadowBg = robot.createScreenCapture(RECT);
                final JComponent parent = (JComponent) contents.getParent();
                parent.putClientProperty(ShadowPopupFactory.PROP_HORIZONTAL_BACKGROUND, hShadowBg);
                parent.putClientProperty(ShadowPopupFactory.PROP_VERTICAL_BACKGROUND, vShadowBg);
                final Container layeredPane = getLayeredPane();
                if (layeredPane == null) {
                    return false;
                }
                POINT.x = x;
                POINT.y = y;
                SwingUtilities.convertPointFromScreen(POINT, layeredPane);
                RECT.x = POINT.x;
                RECT.y = POINT.y + height - SHADOW_SIZE;
                RECT.width = width;
                RECT.height = SHADOW_SIZE;
                paintShadow(hShadowBg, layeredPane);
                RECT.x = POINT.x + width - SHADOW_SIZE;
                RECT.y = POINT.y;
                RECT.width = SHADOW_SIZE;
                RECT.height = height - SHADOW_SIZE;
                paintShadow(vShadowBg, layeredPane);
            } catch (final AWTException e) {
                return true;
            } catch (final SecurityException e) {
                return true;
            }
            return false;
        }
