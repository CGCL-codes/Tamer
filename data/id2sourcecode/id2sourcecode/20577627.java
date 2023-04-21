    private static void drawDefaultCoordinatedSuccessGlyph(Graphics2D g, SuccessCoverageReportController controller, CoordinatedSuccess success, final int x, int y, final int width, final int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        final Color color = controller.computeColorFor(success);
        g2d.setColor(color);
        final int midY = y + (height - TILING_THICKNESS) / 2;
        g2d.fillRect(x, midY, width, TILING_THICKNESS);
        g2d.drawLine(x, y, x, y + height);
        g2d.drawLine(x + width, y, x + width, y + height);
        g2d.dispose();
    }
