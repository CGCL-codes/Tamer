    public void drawStrings(String[] textLines, int textColor, int x, int y, int leftBorder, int rightBorder, int lineHeight, int maxWidth, int layout, Graphics g) {
        if ((layout & Item.LAYOUT_CENTER) == Item.LAYOUT_CENTER) {
            x = leftBorder + (rightBorder - leftBorder) / 2;
        } else if ((layout & Item.LAYOUT_RIGHT) == Item.LAYOUT_RIGHT) {
            x = rightBorder;
        }
        if ((layout & Item.LAYOUT_BOTTOM) == Item.LAYOUT_BOTTOM) {
            if ((layout & Item.LAYOUT_VCENTER) == Item.LAYOUT_VCENTER) {
                y -= this.textFont.getBaselinePosition();
            } else {
                y -= this.textFont.getHeight();
            }
        }
        int anchor = this.style.getAnchorHorizontal();
        String line = this.textRows[this.currentRow];
        g.drawString(line, x, y, Graphics.TOP | anchor);
    }
