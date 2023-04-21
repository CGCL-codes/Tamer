    public void moveBck(Point to) {
        this.setVisible(false);
        transparent = r.createScreenCapture(new Rectangle(to.x, to.y, this.getWidth(), this.getHeight()));
        this.setVisible(true);
    }
