        private void drawErrorBars(Graphics2D g2, int x, int y1, int y2, Cell c) {
            int low = 3;
            int high = 7;
            int halfTab = 2;
            g2.setStroke(new BasicStroke(2 * iLineWidth));
            Color colIn = g2.getColor();
            g2.setColor(Color.magenta);
            int y = (y1 + y2) / 2;
            g2.drawLine(x - low, y, x + high, y);
            g2.drawLine(x - low, y - halfTab, x - low, y + halfTab);
            g2.drawLine(x + high, y - halfTab, x + high, y + halfTab);
            g2.setStroke(new BasicStroke(iLineWidth));
            g2.setColor(colIn);
        }
