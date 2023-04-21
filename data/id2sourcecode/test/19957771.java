        TransparentComponent(final Frame frame, final Rectangle bounds) {
            try {
                screenX = bounds.x;
                screenY = bounds.y;
                background = (new Robot()).createScreenCapture(bounds);
                frame.addComponentListener(new ComponentAdapter() {

                    public void componentShown(ComponentEvent e) {
                        repaint();
                    }

                    public void componentResized(ComponentEvent e) {
                        repaint();
                    }

                    public void componentMoved(ComponentEvent e) {
                        repaint();
                    }
                });
                frame.addWindowFocusListener(new WindowAdapter() {

                    public void windowGainedFocus(WindowEvent e) {
                        refresh();
                    }

                    public void windowLostFocus(WindowEvent e) {
                        refresh();
                    }
                });
            } catch (AWTException e) {
            }
        }
