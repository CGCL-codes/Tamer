    private void prepare_rendering() {
        if (dialog == null) return;
        float dialogW = dialog.getDuration() * secW;
        float dialogH = 0;
        float trackH = 0.0f;
        float channelH = 0.0f;
        float unitW = 0.0f;
        float unitH = 0.0f;
        float textW = 0.0f;
        for (DialogTrack track : dialog.getTracks()) {
            trackH = 0.0f;
            if (track.hasFg()) {
                channelH = 0.0f;
                for (DialogUnit unit : track.getFg().getUnits()) {
                    unitH = BORDER_FG_UNIT_TOP + BORDER_FG_UNIT_MIDDLE + BORDER_FG_UNIT_BOTTOM + FG_UNIT_BAR_HEIGHT;
                    unitW = unit.getDuration() * secW;
                    textW = unitW - BORDER_FG_UNIT_LEFT - BORDER_BG_UNIT_RIGHT;
                    if (textW < 1) textW = 1;
                    unitH += getTextHeight(unit, textW);
                    channelH = Math.max(channelH, unitH);
                    unit.height = unitH;
                    unit.width = unitW;
                }
                track.getFg().height = channelH;
                trackH += channelH;
            }
            if (track.hasBg()) {
                channelH = 0.0f;
                for (DialogUnit unit : track.getBg().getUnits()) {
                    unitH = BORDER_BG_UNIT_TOP + BORDER_BG_UNIT_BOTTOM;
                    unitW = unit.getDuration() * secW;
                    textW = unitW - BORDER_BG_UNIT_LEFT - BORDER_BG_UNIT_RIGHT;
                    unitH += getTextHeight(unit, textW);
                    channelH = Math.max(channelH, unitH);
                    unit.height = unitH;
                    unit.width = unitW;
                }
                track.getBg().height = channelH;
                trackH += channelH;
            }
            dialogH += trackH + TRACK_SEPARATION;
        }
        if (!dialog.getTracks().isEmpty()) dialogH -= dialog.getTracks().size();
        dialogW += BORDER_LEFT + BORDER_RIGHT;
        dialogH += BORDER_TOP + BORDER_BOTTOM;
        this.totalWidth = dialogW;
        this.totalHeight = dialogH;
        this.legendWidth = 0.0f;
        for (DialogChannel channel : dialog.getChannels()) {
            String label = channel.getLabel();
            if (label != null) {
                Rectangle2D bounds = g2d.getFont().getStringBounds(label, frc);
                float width = (float) bounds.getWidth();
                this.legendWidth = Math.max(this.legendWidth, width);
            }
        }
        if (this.legendWidth != 0.0f) {
            this.legendWidth += BORDER_LEFT + BORDER_RIGHT + 2 * BORDER_LEFT;
        }
    }
