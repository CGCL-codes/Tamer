    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int x = (width - backgroundImage.getWidth(null)) / 2;
        int y = (height - backgroundImage.getHeight(null)) / 2;
        g.drawImage(backgroundImage, 5, y, null);
        g.setColor(Color.black);
        g.setFont(new Font("Dialog", Font.PLAIN, 11));
        int stringWidth = g.getFontMetrics().stringWidth(text);
        x = (width - stringWidth) / 2;
        y = (height + 11) / 2;
        g.drawString(text, x, y);
    }
