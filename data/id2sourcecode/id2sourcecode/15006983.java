    public void paintContent(int x, int y, int leftBorder, int rightBorder, Graphics g) {
        if (this.text != null) {
            y += this.textVerticalAdjustment;
            if (this.bitMapFontViewer != null) {
                if (this.isLayoutCenter) {
                    x = leftBorder + (rightBorder - leftBorder) / 2;
                    x += this.textHorizontalAdjustment;
                } else if (this.isLayoutRight) {
                    x = rightBorder;
                    x += this.textHorizontalAdjustment;
                }
                y += this.textVerticalAdjustment;
                this.bitMapFontViewer.paint(x, y, g);
                return;
            }
            g.setFont(this.font);
            g.setColor(this.textColor);
            int lineHeight = this.font.getHeight() + this.paddingVertical;
            int centerX = 0;
            if (this.isLayoutCenter) {
                centerX = leftBorder + (rightBorder - leftBorder) / 2;
                centerX += this.textHorizontalAdjustment;
            }
            x += this.textHorizontalAdjustment;
            leftBorder += this.textHorizontalAdjustment;
            rightBorder += this.textHorizontalAdjustment;
            int clipX = 0;
            int clipY = 0;
            int clipWidth = 0;
            int clipHeight = 0;
            if (this.useSingleLine && this.clipText) {
                clipX = g.getClipX();
                clipY = g.getClipY();
                clipWidth = g.getClipWidth();
                clipHeight = g.getClipHeight();
                g.clipRect(x, y, this.contentWidth, this.contentHeight);
            }
            if (this.textEffect != null) {
                this.textEffect.drawStrings(this.textLines, this.textColor, x, y, leftBorder, rightBorder, lineHeight, this.contentWidth, this.layout, g);
            } else {
                for (int i = 0; i < this.textLines.length; i++) {
                    String line = this.textLines[i];
                    int lineX = x;
                    int lineY = y;
                    int orientation;
                    if (this.isLayoutRight) {
                        lineX = rightBorder;
                        orientation = Graphics.TOP | Graphics.RIGHT;
                    } else if (this.isLayoutCenter) {
                        lineX = centerX;
                        orientation = Graphics.TOP | Graphics.HCENTER;
                    } else {
                        orientation = Graphics.TOP | Graphics.LEFT;
                    }
                    if (this.clipText) {
                        lineX += this.xOffset;
                    }
                    g.drawString(line, lineX, lineY, orientation);
                    x = leftBorder;
                    y += lineHeight;
                }
            }
            if (this.useSingleLine && this.clipText) {
                g.setClip(clipX, clipY, clipWidth, clipHeight);
            }
        }
    }
