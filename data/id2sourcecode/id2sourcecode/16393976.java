    public void paint(int left, int right, int startx, int top, float scale, Graphics g) {
        myx = (left + right) / 2;
        this.startx = startx;
        int hLength = Math.abs(startx - myx);
        this.scale = scale;
        this.top = top;
        bottom = (int) (top + length * scale);
        totalLength = hLength + (bottom - top);
        xPart = (float) hLength / (float) totalLength;
        g.drawLine(myx, top, startx, top);
        g.drawLine(myx, top, myx, bottom);
        g.drawString(name, myx + 3, bottom - 3);
        g.fillOval(myx - 4, bottom - 4, 8, 8);
        int hgap;
        if (children.size() != 0) hgap = (right - left) / (children.size()); else hgap = right - left;
        for (int i = 0; i < children.size(); i++) {
            children.get(i).paint(left + hgap * i, left + hgap * (i + 1), myx, bottom, scale, g);
        }
    }
