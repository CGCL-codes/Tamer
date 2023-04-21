    @Override
    public boolean keyAction(final int actionId, final int[] rows, final int[] cols) {
        boolean actionPerformed = false;
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if (isSelected(row, column)) {
                    if (isRowTiming(row)) {
                    } else {
                        LightCueDetail cue = getDetail(column);
                        if (isRowSubmaster(row)) {
                            CueSubmasterLevel l = cue.getSubmasterLevel(getSubmasterIndex(row));
                            int oldValue = l.getIntValue();
                            int newValue = CellAction.getLevelValue(actionId, oldValue);
                            getCues().setCueSubmaster(getCueIndex(column), getSubmasterIndex(row), newValue / 100f);
                        } else {
                            CueChannelLevel l = cue.getChannelLevel(getChannelIndex(row));
                            int oldValue = l.getChannelIntValue();
                            int newValue = CellAction.getLevelValue(actionId, oldValue);
                            getCues().setChannel(getCueIndex(column), getChannelIndex(row), newValue / 100f);
                        }
                        fireTableCellUpdated(row, column);
                        actionPerformed = true;
                    }
                }
            }
        }
        return actionPerformed;
    }
