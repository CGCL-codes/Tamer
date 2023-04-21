        public void paintControl(PaintEvent event) {
            event.gc.setForeground(colorLightShadow);
            int lineWidth = 1;
            int charLen = 1;
            int rightHalfWidth = 0;
            if (hexContent) {
                lineWidth = fontCharWidth;
                charLen = 3;
                rightHalfWidth = (lineWidth + 1) / 2;
            }
            event.gc.setLineWidth(lineWidth);
            for (int block = 8; block <= myBytesPerLine; block += 8) {
                int xPos = (charLen * block) * fontCharWidth - rightHalfWidth;
                event.gc.drawLine(xPos, event.y, xPos, event.y + event.height);
            }
        }
