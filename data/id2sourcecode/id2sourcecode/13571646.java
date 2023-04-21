    public void paintComponent(Graphics g) {
        if (fontMetrics != null) {
            int lineHeight = getLineHeight();
            Rectangle bounds = g.getClipBounds();
            g.setColor(getBackground());
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setColor(getForeground());
            int startLine = getLineNumber(bounds.y);
            if (startLine > 0) {
                startLine--;
            }
            int endLine = getLineNumber(bounds.y + bounds.height);
            if (endLine < getLines()) {
                endLine++;
            }
            int line = startLine;
            while (line < endLine) {
                try {
                    int start = getLineStart(line);
                    if (start != -1) {
                        if (isFolded(line + 1)) {
                            drawClosedFold(g, getInsets().left, start + ((lineHeight - ICON_WIDTH) / 2));
                        } else if (getLastFoldLine(line, Math.min(getLineNumber(bounds.y + getVisibleRect().height) + 2, getLines() - 1)) != -1) {
                            drawOpenFold(g, getInsets().left, start + ((lineHeight - ICON_WIDTH) / 2));
                        } else if (line > this.start && line < end) {
                            drawLine(g, getInsets().left, start);
                        } else if (line == end) {
                            drawEnd(g, getInsets().left, start);
                        }
                    }
                    line = getNextLineNumber(line);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
