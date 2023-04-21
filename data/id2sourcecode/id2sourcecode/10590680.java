    public void paintContent(int x, int y, int leftBorder, int rightBorder, Graphics g) {
        super.paintContent(x, y, leftBorder, rightBorder, g);
        if (this.isFocused) {
            String head = this.text.substring(0, this.editIndex);
            int headWidth = stringWidth(head);
            char editChar = this.text.charAt(this.editIndex);
            int editWidth = charWidth(editChar);
            if (this.isLayoutCenter) {
                int centerX = leftBorder + (rightBorder - leftBorder) / 2;
                int completeWidth = stringWidth(this.text);
                x = centerX - (completeWidth / 2);
            } else if (this.isLayoutRight) {
                int completeWidth = stringWidth(this.text);
                x = rightBorder - completeWidth;
            }
            g.fillRect(x + headWidth - 1, y - 1, editWidth + 1, getFontHeight());
            if (this.showCaret) {
                g.setColor(this.textComplementColor);
                g.drawChar(editChar, x + headWidth, y + getFontHeight(), Graphics.BOTTOM | Graphics.LEFT);
            }
        }
    }
