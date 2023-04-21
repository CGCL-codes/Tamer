    public void paint(Graphics g) {
        if (down) {
            int iw = downImage.getWidth(this);
            int ih = downImage.getHeight(this);
            int x = (w + iw) / 2;
            int y = (h + ih) / 2;
            g.drawImage(downImage, x, y, this);
        } else {
            if (enabled) {
                int iw = upImage.getWidth(this);
                int ih = upImage.getHeight(this);
                int x = (w + iw) / 2;
                int y = (h + ih) / 2;
                g.drawImage(upImage, x, y, this);
            } else {
                int iw = disabledImage.getWidth(this);
                int ih = disabledImage.getHeight(this);
                int x = (w + iw) / 2;
                int y = (h + ih) / 2;
                g.drawImage(disabledImage, x, y, this);
            }
        }
    }
