    @Override
    public void paint(Graphics g) {
        int off_x, off_y;
        if (off == null || on == null) {
            g.setColor(Color.white);
            g.fillOval(getX() + 2, getY() + 2, 16, 16);
            g.setColor(Color.black);
            g.drawOval(getX() + 2, getY() + 2, 16, 16);
            if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
                g.fillOval(getX() + 6, getY() + 6, 8, 8);
            }
            off_x = 20;
            off_y = 18;
        } else {
            Image img = off;
            if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
                img = on;
            }
            g.drawImage(img, getX(), getY(), null);
            g.setColor(Color.black);
            off_x = img.getWidth(null);
            off_y = img.getHeight(null);
        }
        baseline = (off_y + fontSize) / 2;
        setTextColor(g);
        g.setFont(f);
        g.drawString(text.getStringValue(), getX() + off_x, getY() + baseline - 4);
    }
