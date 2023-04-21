    private void drawMonitor(Graphics g, boolean cleared) {
        Monitorable monitor = calc.getMonitor();
        int actualMonitorRows = actualMonitorRows();
        int actualMonitorCols = 1;
        if (monitor == null || (actualMonitorRows == 0 && !monitor.hasCaption())) return;
        calc.adjustMonitorOffsets(actualMonitorRows, actualMonitorCols);
        monitor.setDisplayedSize(calc.requestedMonitorSize, 1);
        int w = nMonitorDigits * monitorFontWidth;
        for (int col = 0; col < actualMonitorCols; col++) {
            if (monitor.hasCaption()) {
                String caption = monitor.caption(calc.monitorColOffset);
                monitorFont.setMonospaced(false);
                int width = monitorFont.stringWidth(caption);
                int x = monitorOffX + (w - width) / 2;
                monitorFont.drawString(g, x, monitorOffY, caption);
            }
            for (int row = 0; row < actualMonitorRows; row++) {
                int y = monitorOffY + (row + (monitor.hasCaption() ? 1 : 0)) * monitorFontHeight;
                drawLine(g, monitorFont, monitor, row + calc.monitorRowOffset, col + calc.monitorColOffset, monitorOffX, y, w, cleared);
            }
        }
    }
