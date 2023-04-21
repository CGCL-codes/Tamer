    public void paintContent(int x, int y, int leftBorder, int rightBorder, Graphics g) {
        String[] lines = this.textLines;
        if (lines != null) {
            g.setFont(this.font);
            g.setColor(this.textColor);
            int lineHeight = getFontHeight() + this.paddingVertical;
            int centerX = 0;
            boolean isCenter;
            boolean isRight;
            isCenter = this.isLayoutCenter;
            isRight = this.isLayoutRight;
            if (isCenter) {
                centerX = leftBorder + (rightBorder - leftBorder) / 2;
            }
            int lineX = x;
            int lineY = y;
            int orientation;
            if (isRight) {
                lineX = rightBorder;
                orientation = Graphics.RIGHT;
            } else if (isCenter) {
                lineX = centerX;
                orientation = Graphics.HCENTER;
            } else {
                orientation = Graphics.LEFT;
            }
            orientation = Graphics.TOP | orientation;
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                g.drawString(line, lineX, lineY, orientation);
                lineY += lineHeight;
            }
        }
    }
