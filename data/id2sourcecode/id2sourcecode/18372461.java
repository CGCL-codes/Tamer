    protected void paintComponent(Graphics g) {
        Rectangle clipBounds = g.getClipBounds();
        Dimension size = getSize();
        FontMetrics fm = g.getFontMetrics();
        fontHeight = fm.getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);
        if (model == null) {
            return;
        }
        imageBoundsMap.clear();
        int x = gridPadding;
        int y = gridPadding;
        for (int i = 0; i < model.getImageCount(); i++) {
            Image image = model.getImage(i);
            Rectangle bounds = new Rectangle(x, y, gridSize, gridSize);
            imageBoundsMap.put(bounds, i);
            if (image != null && bounds.intersects(clipBounds)) {
                Dimension dim = constrainSize(image, gridSize);
                g.drawImage(image, x + (gridSize - dim.width) / 2, y + (gridSize - dim.height) / 2, dim.width, dim.height, this);
                if (showImageBorder) {
                    g.setColor(Color.black);
                    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
                }
            }
            if (selectedIDList.contains(model.getID(i))) {
                ImageBorder.RED.paintAround((Graphics2D) g, bounds.x, bounds.y, bounds.width, bounds.height);
            }
            if (showCaptions) {
                String caption = model.getCaption(i);
                if (caption != null) {
                    int strWidth = fm.stringWidth(caption);
                    int cx = x + (gridSize - strWidth) / 2;
                    int cy = y + gridSize + fm.getHeight();
                    g.setColor(getForeground());
                    g.drawString(caption, cx, cy);
                }
            }
            x += gridSize + gridPadding;
            if (x > size.width - gridPadding - gridSize) {
                x = gridPadding;
                y += gridSize + gridPadding;
                if (showCaptions) {
                    y += fontHeight;
                }
            }
        }
    }
