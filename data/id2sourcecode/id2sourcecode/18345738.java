        private void paintButtons(Graphics g) {
            int s = 3 * w / 4;
            int x1 = s + w / 32;
            int x2 = w - w / 32;
            int y1 = h / 8;
            int y2 = h - h / 8;
            g.setColor(Color.darkGray);
            g.drawRect(x1, y1, x2 - x1, y2 - y1);
            g.setColor(Color.lightGray);
            g.fillRect(x1 + 1, y1 + 1, x2 - x1 - 2, y2 - y1 - 2);
            int r = Math.min((x2 - x1) / 3, (y2 - y1) / 6);
            int xc = (x1 + x2) / 2;
            int yc1 = (y1 + y2) / 2 - (3 * r / 2);
            int yc2 = (y1 + y2) / 2 + (3 * r / 2);
            g.setColor(Color.red);
            if (floor < numFloors - 1) {
                if (upSet) g.fillOval(xc - r, yc1 - r, 2 * r, 2 * r); else g.drawOval(xc - r, yc1 - r, 2 * r, 2 * r);
            }
            if (floor > 0) {
                if (downSet) g.fillOval(xc - r, yc2 - r, 2 * r, 2 * r); else g.drawOval(xc - r, yc2 - r, 2 * r, 2 * r);
            }
        }
