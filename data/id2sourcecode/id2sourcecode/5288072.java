        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (sample == null) return;
            int nHeight = getHeight();
            int startX = (int) (sample.getStartOffset() * horizontalZoom);
            int endX = (int) (sample.getEndOffset() * horizontalZoom);
            Rectangle clip = g.getClipBounds();
            int nWidth = clip.width + clip.x;
            int nChannels = sample.getFloatSampleBuffer().getChannelCount();
            g.setColor(ColorScheme.SLICER_WAVEFORM_CLIPPED_BACKGROUND);
            g.fillRect(0, 0, startX, nHeight);
            g.fillRect(endX, 0, nWidth - endX, nHeight);
            System.out.println(horizontalZoom);
            g.setColor(getForeground());
            for (int c = 0; c < nChannels; c++) {
                for (int x = clip.x; x < clip.x + clip.width; x++) {
                    int i = (int) (x / horizontalZoom);
                    int i2 = (int) ((x + 1) / horizontalZoom);
                    float sum = 0;
                    for (int j = i; j < i2; j++) {
                        sum += Math.abs(sample.getFloatSampleBuffer().getChannel(c)[j]);
                    }
                    sum /= i2 - i;
                    int value = (int) (sum * nHeight / nChannels);
                    int y1 = (nHeight * c / nChannels) + (nHeight - 2 * value) / 2;
                    int y2 = y1 + 2 * value;
                    g.drawLine(x, y1, x, y2);
                }
            }
            for (SnapPoint point : sample.getSnapPoints()) {
                int offset = point.getOffset();
                offset = (int) (offset * horizontalZoom);
                Color c = point == spSelected ? getForeground().brighter() : getForeground();
                g.setColor(c);
                g.drawLine(offset, 0, offset, nHeight);
            }
        }
