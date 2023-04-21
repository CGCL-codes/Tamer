    @Override
    public void paintComponent(Graphics g) {
        Rectangle clipBounds = g.getClipBounds();
        int rangeStart = 0;
        int rangeEnd;
        int[] zones = { getCursorPosition() - 1, getCursorPosition() + 2, -1, -1 };
        if (selection != null) {
            zones[2] = selection.begin;
            zones[3] = selection.end + 1;
        }
        while (rangeStart < clipBounds.getWidth()) {
            int rangePosition = (clipBounds.x + rangeStart);
            rangeEnd = clipBounds.width;
            for (int i = 0; i < zones.length; i++) {
                int zonePosition = zones[i];
                if ((zonePosition > rangePosition) && (rangeEnd > zonePosition - clipBounds.x)) {
                    rangeEnd = zonePosition - clipBounds.x;
                }
            }
            if ((rangePosition >= zones[0]) && (rangePosition < zones[1])) {
                g.setColor(Color.BLACK);
            } else if ((rangePosition >= zones[2]) && (rangePosition < zones[3])) {
                g.setColor(selectionColor);
            } else g.setColor(getBackground());
            g.fillRect(rangePosition, clipBounds.y, rangeEnd + clipBounds.x - 1, clipBounds.height);
            rangeStart = rangeEnd;
        }
        int stopPos = clipBounds.width + clipBounds.x;
        if (wave != null) {
            g.setColor(Color.BLACK);
            int channelsCount = wave.getAudioFormat().getChannels();
            int[] prev = { -1, -1 };
            if (stopPos >= wave.getLengthInTicks()) stopPos = wave.getLengthInTicks() - 1;
            for (int pos = clipBounds.x - 1; pos <= stopPos; pos++) {
                for (int channel = 0; channel < channelsCount; channel++) {
                    int pomPos = pos;
                    if (pomPos < 0) pomPos = 0;
                    int value = wave.getRatioValue(pomPos, channel, getHeight() / channelsCount) + (channel * getHeight()) / channelsCount;
                    int middle = (getHeight() + (2 * channel * getHeight())) / (2 * channelsCount);
                    switch(drawMode) {
                        case DOTS_MODE:
                            {
                                g.drawLine(pos, value, pos, value);
                                break;
                            }
                        case LINE_MODE:
                            {
                                if (prev[channel] >= 0) {
                                    g.drawLine(pos - 1, prev[channel], pos, value);
                                }
                                prev[channel] = value;
                                break;
                            }
                        case INTEGRAL_MODE:
                            {
                                g.drawLine(pos, value, pos, middle);
                                break;
                            }
                    }
                }
            }
        }
    }
