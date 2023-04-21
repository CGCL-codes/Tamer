        public void paintComponent(Graphics g) {
            if (point != null) {
                Rectangle rect = new Rectangle((int) point.getX() - 10, (int) point.getY() - 10, 20, 20);
                try {
                    BufferedImage img = new Robot().createScreenCapture(rect);
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                    int oldColor = activeColor;
                    activeColor = img.getRGB(img.getWidth() / 2, img.getHeight() / 2);
                    firePropertyChange("activeColor", oldColor, activeColor);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
            g.setColor(Color.black);
            g.drawRect(getWidth() / 2 - 5, getHeight() / 2 - 5, 10, 10);
        }
