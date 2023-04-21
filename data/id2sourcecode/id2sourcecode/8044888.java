        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Dimension s = getSize();
            int x = (s.width - RECT_WIDTH) / 2;
            int y = (s.height - RECT_HEIGHT) / 2;
            g2.setColor(Color.WHITE);
            g2.fill(new Double(5, 5, s.width - 10, s.height - 10, 10, 10));
            g2.setStroke(new BasicStroke(3.0f, 0, 0, 2.0f, new float[] { 10.0f }, 0.0f));
            g2.setColor(Color.LIGHT_GRAY);
            g2.fill(new Double(x, y, RECT_WIDTH, RECT_HEIGHT, 10, 10));
            int width = g2.getFontMetrics().stringWidth(string);
            int height = g2.getFontMetrics().getAscent();
            int sx = x + (RECT_WIDTH - width) / 2;
            int sy = y + (RECT_HEIGHT) / 2;
            g2.setColor(Color.WHITE);
            g2.drawString(string, sx, sy);
        }
