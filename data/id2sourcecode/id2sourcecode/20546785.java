    protected void drawUnselected(int index, GC gc, Rectangle bounds, int state) {
        CTabItem item = parent.items[index];
        int x = bounds.x;
        int y = bounds.y;
        int height = bounds.height;
        int width = bounds.width;
        if (!item.showing) return;
        Rectangle clipping = gc.getClipping();
        if (!clipping.intersects(bounds)) return;
        if ((state & SWT.BACKGROUND) != 0) {
            if (index > 0 && index < parent.selectedIndex) drawLeftUnselectedBorder(gc, bounds, state);
            if (index > parent.selectedIndex) drawRightUnselectedBorder(gc, bounds, state);
        }
        if ((state & SWT.FOREGROUND) != 0) {
            Rectangle trim = computeTrim(index, SWT.NONE, 0, 0, 0, 0);
            int xDraw = x - trim.x;
            Image image = item.getImage();
            if (image != null && parent.showUnselectedImage) {
                Rectangle imageBounds = image.getBounds();
                int maxImageWidth = x + width - xDraw - (trim.width + trim.x);
                if (parent.showUnselectedClose && (parent.showClose || item.showClose)) {
                    maxImageWidth -= item.closeRect.width + INTERNAL_SPACING;
                }
                if (imageBounds.width < maxImageWidth) {
                    int imageX = xDraw;
                    int imageHeight = imageBounds.height;
                    int imageY = y + (height - imageHeight) / 2;
                    imageY += parent.onBottom ? -1 : 1;
                    int imageWidth = imageBounds.width * imageHeight / imageBounds.height;
                    gc.drawImage(image, imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height, imageX, imageY, imageWidth, imageHeight);
                    xDraw += imageWidth + INTERNAL_SPACING;
                }
            }
            int textWidth = x + width - xDraw - (trim.width + trim.x);
            if (parent.showUnselectedClose && (parent.showClose || item.showClose)) {
                textWidth -= item.closeRect.width + INTERNAL_SPACING;
            }
            if (textWidth > 0) {
                Font gcFont = gc.getFont();
                gc.setFont(item.font == null ? parent.getFont() : item.font);
                if (item.shortenedText == null || item.shortenedTextWidth != textWidth) {
                    item.shortenedText = shortenText(gc, item.getText(), textWidth);
                    item.shortenedTextWidth = textWidth;
                }
                Point extent = gc.textExtent(item.shortenedText, FLAGS);
                int textY = y + (height - extent.y) / 2;
                textY += parent.onBottom ? -1 : 1;
                gc.setForeground(parent.getForeground());
                gc.drawText(item.shortenedText, xDraw, textY, FLAGS);
                gc.setFont(gcFont);
            }
            if (parent.showUnselectedClose && (parent.showClose || item.showClose)) drawClose(gc, item.closeRect, item.closeImageState);
        }
    }
