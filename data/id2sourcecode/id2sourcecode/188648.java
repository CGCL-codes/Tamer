    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        lineHeight = getHeight() / 2;
        g.setColor(REFERENCE_LINE_COLOR);
        g.drawLine(0, lineHeight, (int) getWidth(), lineHeight);
        drawWaveform(g, aa.getChannel(channelIndex));
    }
