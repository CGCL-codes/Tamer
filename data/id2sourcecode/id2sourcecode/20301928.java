    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
        CueChannelLevel cueLevel = (CueChannelLevel) value;
        int intValue = cueLevel.getChannelIntValue();
        level(cueLevel.isActive(), intValue);
        return editorComponent;
    }
