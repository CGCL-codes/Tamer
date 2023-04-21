    public void drawStrings(String[] textLines, int textColor, int x, int y, int leftBorder, int rightBorder, int lineHeight, int maxWidth, int layout, Graphics g) {
        if (textLines != this.lastText && this.font != null) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < textLines.length; i++) {
                buffer.append(textLines[i]);
                if (i != textLines.length - 1) {
                    buffer.append('\n');
                }
            }
            this.viewer = this.font.getViewer(buffer.toString(), textColor);
            this.lastText = textLines;
        }
        if (this.viewer == null) {
            super.drawStrings(textLines, textColor, x, y, leftBorder, rightBorder, lineHeight, maxWidth, layout, g);
        } else {
            if ((layout & Item.LAYOUT_CENTER) == Item.LAYOUT_CENTER) {
                x = leftBorder + (rightBorder - leftBorder) / 2;
            } else if ((layout & Item.LAYOUT_RIGHT) == Item.LAYOUT_RIGHT) {
                x = rightBorder;
            }
            if ((layout & Item.LAYOUT_BOTTOM) == Item.LAYOUT_BOTTOM) {
                int fontHeight = this.font.getFontHeight();
                if ((layout & Item.LAYOUT_VCENTER) == Item.LAYOUT_VCENTER) {
                    y -= fontHeight / 2;
                } else {
                    y -= fontHeight;
                }
            }
            this.viewer.paint(x, y, g);
        }
    }
