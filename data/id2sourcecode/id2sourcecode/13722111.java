    @Override
    public void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        int col = getWidth() / zoomRatio;
        int row = getHeight() / zoomRatio;
        Point mousePos = data.getMousePosition();
        BufferedImage zone = picker.createScreenCapture(new Rectangle(mousePos.x - col / 2, mousePos.y - row / 2, col, row));
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(zone, 0, 0, col * zoomRatio, row * zoomRatio, 0, 0, col, row, this);
        g.setColor(Color.black);
        g.drawLine(col * zoomRatio / 2, row * zoomRatio / 2 - 3, col * zoomRatio / 2, row * zoomRatio / 2 + 3);
        g.drawLine(col * zoomRatio / 2 - 3, row * zoomRatio / 2, col * zoomRatio / 2 + 3, row * zoomRatio / 2);
    }
