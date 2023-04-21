    protected void drawWaveform(Graphics g, short[] samples) {
        if (samples == null) {
            return;
        }
        int X = 0;
        int oldY = getHeight() - ((getHeight() * (samples[0] + 32768)) / 65536);
        int newY;
        increment = (int) (aa.getChannelLength() / getWidth());
        inc_helper = (int) (increment / zoomScale);
        if (increment < 1) drawPoints = true; else drawPoints = false;
        for (int i = 1; i < samples.length; i++) {
            newY = getHeight() - ((getHeight() * (samples[i] + 32768)) / 65536);
            if (i == increment) {
                increment += (inc_helper / zoomScale);
                if (highlighted) {
                    if ((X >= pressPoint.x && X <= dragPoint.x) || (X >= dragPoint.x && X <= pressPoint.x)) {
                        g.setColor(HIGHLIGHT);
                        g.drawLine(X, oldY, X++, newY);
                        g.setColor(HIGHLIGHT_BCKGRD);
                        if (oldY > newY) {
                            g.drawLine(X, oldY, X, 0);
                            g.drawLine(X, newY, X, getHeight());
                        } else {
                            g.drawLine(X, newY, X, 0);
                            g.drawLine(X, oldY, X, getHeight());
                        }
                    } else {
                        g.setColor(WAVE);
                        g.drawLine(X, oldY, X++, newY);
                    }
                } else {
                    g.setColor(WAVE);
                    g.drawLine(X, oldY, X++, newY);
                    if (drawCursor) {
                        if (X == pixelNumber) {
                            g.setColor(Color.black);
                            g.drawLine(X, 0, X, getHeight());
                        }
                    }
                }
                oldY = newY;
            }
        }
    }
