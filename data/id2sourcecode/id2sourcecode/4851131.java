    private void keyActionSubmasterLevel(final int actionId, final int row, final int col) {
        int channelIndex = getChannelIndex(row);
        Level level = getSelectedSubmaster().getLevel(channelIndex);
        int value = level.getIntValue();
        value = CellAction.getLevelValue(actionId, value);
        level.setIntValue(value);
        level.setActive(true);
        updateSelectedSubmasterLevel(channelIndex, level.getValue());
        fireTableCellUpdated(row, col);
    }
