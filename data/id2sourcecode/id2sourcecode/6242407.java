    void drawUnselected(GC gc) {
        String displayStr = showing ? "" : "none";
        if (textHandle != null) {
            textHandle.style.display = displayStr;
        }
        if (imageHandle != null) {
            imageHandle.style.display = displayStr;
        }
        if (seperatorLine != null) {
            seperatorLine.style.display = displayStr;
        }
        if (closeBtn != null) {
            closeBtn.style.display = displayStr;
        }
        if (!showing) return;
        int index = parent.indexOf(this);
        if (index > 0 && index < parent.selectedIndex) drawLeftUnselectedBorder(gc);
        if (index > parent.selectedIndex) drawRightUnselectedBorder(gc);
        if (seperatorLine != null) {
            seperatorLine.style.display = "";
            if (index == 0 && index < parent.selectedIndex) {
                seperatorLine.style.display = "none";
            }
        }
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != null) {
                elements[i].style.display = "none";
            }
        }
        int xDraw = x + LEFT_MARGIN;
        Image image = getImage();
        if (image != null && parent.showUnselectedImage) {
            Rectangle imageBounds = image.getBounds();
            int maxImageWidth = x + width - xDraw - RIGHT_MARGIN;
            if (parent.showUnselectedClose && (parent.showClose || showClose)) {
                maxImageWidth -= closeRect.width + INTERNAL_SPACING;
            }
            if (imageBounds.width < maxImageWidth) {
                int imageX = xDraw;
                int imageHeight = imageBounds.height;
                int imageY = y + (height - imageHeight) / 2;
                imageY += parent.onBottom ? -1 : 1;
                int imageWidth = imageBounds.width * imageHeight / imageBounds.height;
                drawImage(image, imageX, imageY, imageWidth, imageHeight);
                xDraw += imageWidth + INTERNAL_SPACING;
            }
        } else {
            if (imageHandle != null) {
                handle.style.display = "none";
            }
        }
        int textWidth = x + width - xDraw - RIGHT_MARGIN;
        if (parent.showUnselectedClose && (parent.showClose || showClose)) {
            textWidth -= closeRect.width + INTERNAL_SPACING;
        }
        if (textWidth > 0) {
            if (shortenedText == null || shortenedTextWidth != textWidth) {
                shortenedText = shortenText(getText(), textWidth);
                shortenedTextWidth = textWidth;
            }
            Point extent = textExtent(shortenedText, FLAGS);
            int textY = y + (height - extent.y) / 2;
            textY += parent.onBottom ? -1 : 1;
            drawText(shortenedText, xDraw, textY, FLAGS);
        }
        if (parent.showUnselectedClose && (parent.showClose || showClose)) drawClose(gc);
    }
