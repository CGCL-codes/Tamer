    @Override
    public void paint(Graphics g) {
        Image img;
        int off_x;
        int off_y;
        if (on == null || off == null) {
            g.setColor(Color.white);
            g.fillRect(getX() + 2, getY() + 2, 16, 16);
            g.setColor(Color.black);
            g.drawRect(getX() + 2, getY() + 2, 16, 16);
            if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
                g.drawLine(getX() + 2, getY() + 2, getX() + 18, getY() + 18);
                g.drawLine(getX() + 2, getY() + 18, getX() + 18, getY() + 2);
            }
            off_x = 20;
            off_y = 18;
        } else {
            if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
                img = on;
            } else {
                img = off;
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
