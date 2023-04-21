    public void scroll(final int delta) {
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if (isSelected(row, column)) {
                    LightCueDetail detail = (LightCueDetail) getCue(column).getDetail();
                    if (isRowTiming(row)) {
                        Timing timing = detail.getTiming();
                        if (!timing.isSplitFade()) {
                            Time time = timing.getFadeInTime();
                            int id = time.getId();
                            id += delta;
                            if (id < 0) {
                                id = 0;
                            } else if (id > Time.FOREVER.getId()) {
                                id = Time.FOREVER.getId();
                            }
                            time = Time.get(id);
                            timing.setFadeInTime(time);
                            fireTableCellUpdated(row, column);
                        }
                    } else if (isRowSubmaster(row)) {
                        int submasterIndex = getSubmasterIndex(row);
                        float value = detail.getSubmasterLevel(submasterIndex).getValue();
                        value = delta(value, delta);
                        getCues().setCueSubmaster(getCueIndex(column), submasterIndex, value);
                        fireTableCellUpdated(row, column);
                    } else {
                        int channelIndex = getChannelIndex(row);
                        float value = detail.getChannelLevel(channelIndex).getChannelLevelValue().getValue();
                        value = delta(value, delta);
                        getCues().setChannel(getCueIndex(column), channelIndex, value);
                        fireTableCellUpdated(row, column);
                    }
                }
            }
        }
    }
