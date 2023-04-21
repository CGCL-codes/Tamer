        public void paintComponent(Graphics g) {
            g.drawImage(bi, 0, 0, width, height, this);
            g.setColor(Color.RED);
            g.drawLine(startX, startY, endX, startY);
            g.drawLine(startX, endY, endX, endY);
            g.drawLine(startX, startY, startX, endY);
            g.drawLine(endX, startY, endX, endY);
            int x = startX < endX ? startX : endX;
            int y = startY < endY ? startY : endY;
            select = new Rectangle(x, y, Math.abs(endX - startX), Math.abs(endY - startY));
            int x1 = (startX + endX) / 2;
            int y1 = (startY + endY) / 2;
            g.fillRect(x1 - 2, startY - 2, 5, 5);
            g.fillRect(x1 - 2, endY - 2, 5, 5);
            g.fillRect(startX - 2, y1 - 2, 5, 5);
            g.fillRect(endX - 2, y1 - 2, 5, 5);
            g.fillRect(startX - 2, startY - 2, 5, 5);
            g.fillRect(startX - 2, endY - 2, 5, 5);
            g.fillRect(endX - 2, startY - 2, 5, 5);
            g.fillRect(endX - 2, endY - 2, 5, 5);
            rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
            rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
            rec[2] = new Rectangle((startX > endX ? startX : endX) - 5, y - 5, 10, 10);
            rec[3] = new Rectangle((startX > endX ? startX : endX) - 5, y1 - 5, 10, 10);
            rec[4] = new Rectangle((startX > endX ? startX : endX) - 5, (startY > endY ? startY : endY) - 5, 10, 10);
            rec[5] = new Rectangle(x1 - 5, (startY > endY ? startY : endY) - 5, 10, 10);
            rec[6] = new Rectangle(x - 5, (startY > endY ? startY : endY) - 5, 10, 10);
            rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);
            if (showTip) {
                g.setColor(Color.CYAN);
                g.fillRect(p.x, p.y, 170, 20);
                g.setColor(Color.RED);
                g.drawRect(p.x, p.y, 170, 20);
                g.setColor(Color.BLACK);
                g.drawString("请按住鼠标左键不放选择截图区", p.x, p.y + 15);
            }
        }
