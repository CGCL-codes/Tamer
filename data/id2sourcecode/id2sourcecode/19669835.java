    public void drawStrings(String[] textLines, int textColor, int x, int y, int leftBorder, int rightBorder, int lineHeight, int maxWidth, int layout, Graphics g) {
        boolean isLayoutRight = false;
        boolean isLayoutCenter = false;
        int centerX = 0;
        if ((layout & Item.LAYOUT_CENTER) == Item.LAYOUT_CENTER) {
            isLayoutCenter = true;
            centerX = leftBorder + (rightBorder - leftBorder) / 2;
        } else if ((layout & Item.LAYOUT_RIGHT) == Item.LAYOUT_RIGHT) {
            isLayoutRight = true;
        }
        for (int i = 0; i < textLines.length; i++) {
            String line = textLines[i];
            int lineX = x;
            int lineY = y;
            int anchor = 0;
            if (isLayoutRight) {
                lineX = rightBorder;
                anchor = Graphics.TOP | Graphics.RIGHT;
            } else if (isLayoutCenter) {
                lineX = centerX;
                anchor = Graphics.TOP | Graphics.HCENTER;
            } else {
                anchor = Graphics.TOP | Graphics.LEFT;
            }
            drawString(line, textColor, lineX, lineY, anchor, g);
            x = leftBorder;
            y += lineHeight;
        }
    }
