    @Override
    public void paintComponent(final Graphics graphics) {
        synchronized (this) {
            int plotLength;
            final int thresholdLine;
            super.paintComponent(graphics);
            final Graphics2D graphics2d = (Graphics2D) graphics;
            final double value = monitor.getValue();
            final double threshold = monitor.getThreshold();
            final double maximum = monitor.getMaximum();
            final Dimension dim = getSize();
            final int length = dim.width - 2 * BORDER_END;
            final int height = dim.height - 2 * BORDER_SIDE;
            if (maximum > 0) {
                plotLength = (int) (length * value / maximum);
                thresholdLine = (int) (length * threshold / maximum);
            } else {
                plotLength = 0;
                thresholdLine = 0;
            }
            if (plotLength >= length) {
                plotLength = length - 1;
            }
            graphics2d.setColor(SystemColor.control);
            graphics2d.fillRect(BORDER_END, BORDER_SIDE, length, height - 1);
            graphics2d.setColor(SystemColor.textText);
            graphics2d.drawRect(BORDER_END, BORDER_SIDE, length, height - 1);
            final Color barColor = (value < threshold) || (value > maximum) ? Color.RED : Color.GREEN;
            graphics2d.setColor(barColor);
            graphics2d.fillRect(BORDER_END + 1, BORDER_SIDE + 1, plotLength, height - 2);
            graphics2d.setColor(SystemColor.textText);
            graphics2d.drawLine(BORDER_END + thresholdLine, BORDER_SIDE + (height / 2), BORDER_END + thresholdLine, BORDER_SIDE + height - 1);
            final int midx = (BORDER_END + length) / 2;
            final int midy = (BORDER_SIDE + height) / 2;
            final String sValue = String.valueOf(value);
            final FontRenderContext frc = graphics2d.getFontRenderContext();
            final Rectangle bounds = graphics2d.getFont().getStringBounds(sValue, frc).getBounds();
            graphics2d.drawString(sValue, midx - bounds.width / 2, midy + bounds.height / 2);
        }
    }
