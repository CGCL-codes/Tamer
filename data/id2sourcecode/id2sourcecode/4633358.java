    public void paint(Graphics g) {
        super.paint(g);
        Rectangle rect = this.getBounds();
        Point loc = getLocationOnScreen();
        rect.x = loc.x + rect.width;
        rect.y = loc.y;
        Image im = sRobot.createScreenCapture(rect);
        g.drawImage(im, 0, 0, null);
        if (mXORMode) {
            g.setXORMode(Color.BLACK);
        }
        int width = getWidth();
        int height = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);
        g.setColor(getForeground());
        if (mXORMode) {
            g.setXORMode(Color.WHITE);
        }
        int wid = width / 10;
        int ht = height / 10;
        g.drawArc(wid / 2, ht / 2, wid * 9, ht * 9, 0, -30);
        g.drawArc(wid / 2, ht / 2, wid * 9, ht * 9, 0, 300);
        int ptx1 = (int) (width / 2 + wid * 18 / 4.0 * (Math.cos(30 * Math.PI / 180)));
        int pty1 = (int) (height / 2 + ht * 18 / 4.0 * (Math.sin(30 * Math.PI / 180)));
        int ptx2 = (int) (width / 2 + wid * 18 / 4.0 * (Math.cos(60 * Math.PI / 180)));
        int pty2 = (int) (height / 2 + ht * 18 / 4.0 * (Math.sin(60 * Math.PI / 180)));
        g.drawLine(ptx1, pty1, width, height);
        g.drawLine(ptx2, pty2, width, height);
        if (mTxt != null) {
            int x = (int) ((width / 2) * (1 - Math.sqrt(2) / 2));
            int y = (int) ((height / 2) + (height / 2 * Math.sqrt(2) / 2));
            int sqwidth = (int) (width * Math.sqrt(2) / 2);
            int sqheight = (y - height / 2) * 2;
            System.out.println("x = " + x + " y = " + y);
            Font f = g.getFont();
            FontMetrics fm = g.getFontMetrics();
            float pt = f.getSize();
            while (fm.stringWidth(mTxt) < sqwidth && fm.getHeight() < sqheight) {
                pt += 2;
                f = f.deriveFont(pt);
                fm = g.getFontMetrics(f);
            }
            int x_diff = sqwidth - fm.stringWidth(mTxt);
            int y_diff = sqheight - fm.getHeight();
            g.setFont(f);
            g.drawString(mTxt, x + x_diff / 2, y - y_diff / 2 - fm.getHeight() / 5);
        }
    }
