    protected void paintComponent(Graphics context) {
        context.setColor(PANEL_COLOR);
        context.fillRect(0, 0, getWidth(), getHeight());
        if (HexEditor.dumpWidth != panelDumpWidth || !HexEditor.fontName.equals(panelFontName) || getWidth() != panelWidth) {
            panelDumpWidth = HexEditor.dumpWidth;
            panelFontName = HexEditor.fontName;
            panelHeight = getHeight();
            panelWidth = getWidth();
            panelColumns = 9 + panelDumpWidth * 3 + 2 + 1 + panelDumpWidth + 1;
            int fontSizeLow = 10;
            int fontSizeHigh = 73;
            panelFontSize = -1;
            do {
                int testSize = (fontSizeLow + fontSizeHigh) / 2;
                if (panelFontSize == testSize) {
                    break;
                }
                panelFontSize = testSize;
                panelFont = new Font(panelFontName, 1, panelFontSize);
                FontMetrics fm = context.getFontMetrics(panelFont);
                lineAscent = fm.getAscent();
                lineHeight = fm.getHeight();
                maxWidth = -1;
                for (int i = 32; i <= 126; i++) {
                    charWidths[i] = fm.charWidth(i);
                    maxWidth = Math.max(charWidths[i], maxWidth);
                }
                if (panelColumns * maxWidth < panelWidth - 20) {
                    fontSizeLow = panelFontSize;
                } else {
                    fontSizeHigh = panelFontSize;
                }
            } while (true);
            for (int i = 32; i <= 126; i++) {
                charShifts[i] = (maxWidth - charWidths[i]) / 2;
            }
            panelRows = Math.max(1, (panelHeight - 20) / lineHeight);
            adjustScrollBar();
        } else if (getHeight() != panelHeight) {
            panelHeight = getHeight();
            panelRows = Math.max(1, (panelHeight - 20) / lineHeight);
            adjustScrollBar();
        }
        int panelNibbleBegin = panelOffset * 2;
        int panelNibbleEnd = panelNibbleBegin + panelRows * panelDumpWidth * 2;
        int selectBegin = Math.max(panelNibbleBegin, Math.min(cursorDot, cursorMark));
        int selectEnd = Math.min(panelNibbleEnd, Math.max(cursorDot, cursorMark));
        if (selectBegin < selectEnd) {
            context.setColor(cursorOnText ? SHADOW_SELECT : ACTIVE_SELECT);
            int row = (selectBegin / 2 - panelOffset) / panelDumpWidth;
            int thisRowY = 10 + row * lineHeight;
            int rowNibbleCount = panelDumpWidth * 2;
            int nibble = selectBegin % rowNibbleCount;
            int rowFirstDumpX = 10 + maxWidth * 10;
            int thisColumnX = rowFirstDumpX + maxWidth * (nibble + nibble / 2);
            int nibbleIndex = selectBegin;
            do {
                if (nibbleIndex >= selectEnd) {
                    break;
                }
                context.fillRect(thisColumnX, thisRowY, maxWidth, lineHeight);
                nibble++;
                nibbleIndex++;
                thisColumnX += maxWidth;
                if (nibble >= rowNibbleCount) {
                    nibble = 0;
                    thisColumnX = rowFirstDumpX;
                    thisRowY += lineHeight;
                } else if (nibbleIndex < selectEnd && nibbleIndex % 2 == 0) {
                    context.fillRect(thisColumnX, thisRowY, maxWidth, lineHeight);
                    thisColumnX += maxWidth;
                }
            } while (true);
            context.setColor(cursorOnText ? ACTIVE_SELECT : SHADOW_SELECT);
            row = (selectBegin / 2 - panelOffset) / panelDumpWidth;
            thisRowY = 10 + row * lineHeight;
            int column = (selectBegin % (panelDumpWidth * 2)) / 2;
            int rowFirstTextX = 10 + maxWidth * (8 + 3 * panelDumpWidth + 4);
            thisColumnX = rowFirstTextX + maxWidth * column;
            nibbleIndex = selectBegin - selectBegin % 2;
            do {
                if (nibbleIndex >= selectEnd) {
                    break;
                }
                context.fillRect(thisColumnX, thisRowY, maxWidth, lineHeight);
                column++;
                nibbleIndex += 2;
                thisColumnX += maxWidth;
                if (column >= panelDumpWidth) {
                    column = 0;
                    thisColumnX = rowFirstTextX;
                    thisRowY += lineHeight;
                }
            } while (true);
        }
        if (cursorDot >= panelNibbleBegin && cursorDot < panelNibbleEnd) {
            int cursorY = (cursorDot - panelNibbleBegin) / (panelDumpWidth * 2);
            cursorY = 10 + cursorY * lineHeight;
            int cursorX = (cursorDot % (panelDumpWidth * 2)) / 2;
            cursorX = 10 + maxWidth * (8 + 3 * cursorX + 2);
            if (cursorDot % 2 > 0) {
                cursorX += maxWidth;
            }
            context.setColor(cursorOnText ? SHADOW_CURSOR : ACTIVE_CURSOR);
            if (HexEditor.overFlag) {
                context.drawRect(cursorX - 1, cursorY, maxWidth + 1, lineHeight - 1);
                if (panelFontSize > 24) {
                    context.drawRect(cursorX, cursorY + 1, maxWidth - 1, lineHeight - 3);
                }
            } else {
                context.fillRect(cursorX - 1, cursorY, panelFontSize <= 24 ? 2 : 3, lineHeight);
            }
            cursorX = (cursorDot % (panelDumpWidth * 2)) / 2;
            cursorX = 10 + maxWidth * (8 + 3 * panelDumpWidth + cursorX + 4);
            context.setColor(cursorOnText ? ACTIVE_CURSOR : SHADOW_CURSOR);
            if (HexEditor.overFlag) {
                context.drawRect(cursorX - 1, cursorY, maxWidth + 1, lineHeight - 1);
                if (panelFontSize > 24) {
                    context.drawRect(cursorX, cursorY + 1, maxWidth - 1, lineHeight - 3);
                }
            } else {
                context.fillRect(cursorX - 1, cursorY, panelFontSize <= 24 ? 2 : 3, lineHeight);
            }
        }
        context.setColor(TEXT_COLOR);
        context.setFont(panelFont);
        int maxOffset = HexEditor.nibbleCount / 2;
        int rowLastDigitX = 10 + 7 * maxWidth;
        int rowLeftMarkerX = 10 + maxWidth * (11 + 3 * panelDumpWidth) + charShifts[124];
        int rowRightMarkerX = rowLeftMarkerX + maxWidth * (panelDumpWidth + 1);
        int rowY = 10 + lineAscent;
        int thisOffset = panelOffset;
        for (int row = 0; row < panelRows && thisOffset <= maxOffset; row++) {
            int shiftedOffset = thisOffset;
            int thisDigitX = rowLastDigitX;
            for (int i = 8; i > 0; i--) {
                char ch = HexEditor.HEX_DIGITS[shiftedOffset & 0xf];
                context.drawString(Character.toString(ch), thisDigitX + charShifts[ch], rowY);
                shiftedOffset >>= 4;
                thisDigitX -= maxWidth;
            }
            context.drawString(HexEditor.MARKER_STRING, rowLeftMarkerX, rowY);
            context.drawString(HexEditor.MARKER_STRING, rowRightMarkerX, rowY);
            rowY += lineHeight;
            thisOffset += panelDumpWidth;
        }
        int nibbleIndex = panelOffset * 2;
        int rowFirstDumpX = 10 + maxWidth * 10;
        int rowFirstTextX = rowFirstDumpX + maxWidth * (3 * panelDumpWidth + 2);
        rowY = 10 + lineAscent;
        for (int row = 0; row < panelRows; row++) {
            int thisDumpX = rowFirstDumpX;
            int thisTextX = rowFirstTextX;
            for (int column = 0; column < panelDumpWidth && nibbleIndex < HexEditor.nibbleCount; column++) {
                int thisNibble = HexEditor.nibbleData.get(nibbleIndex++);
                char ch = HexEditor.HEX_DIGITS[thisNibble & 0xf];
                context.drawString(Character.toString(ch), thisDumpX + charShifts[ch], rowY);
                thisDumpX += maxWidth;
                int byteValue = thisNibble << 4;
                if (nibbleIndex < HexEditor.nibbleCount) {
                    thisNibble = HexEditor.nibbleData.get(nibbleIndex++);
                    ch = HexEditor.HEX_DIGITS[thisNibble & 0xf];
                    context.drawString(Character.toString(ch), thisDumpX + charShifts[ch], rowY);
                    thisDumpX += maxWidth;
                    byteValue |= thisNibble;
                } else {
                    byteValue = -1;
                }
                thisDumpX += maxWidth;
                if (byteValue < 32 || byteValue > 126) {
                    byteValue = 46;
                }
                context.drawString(Character.toString((char) byteValue), thisTextX + charShifts[byteValue], rowY);
                thisTextX += maxWidth;
            }
            rowY += lineHeight;
        }
    }
