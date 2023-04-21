    public Object getValueAt(final int row, final int col) {
        AttributeDefinition attribute = fixtureHolder.getValue().getAttribute(row);
        String hasValues = attribute.getValueCount() > 0 ? "*" : "";
        String presetValue = "";
        PresetDefinition preset = presetHolder.getValue();
        if (preset != null) {
            presetValue = preset.getValue(attribute.getName());
        }
        Object value;
        switch(col) {
            case COLUMN_NUMBER:
                value = row + 1;
                break;
            case COLUMN_NAME:
                value = attribute.getName();
                break;
            case COLUMN_CHANNELS:
                value = attribute.getChannels();
                break;
            case COLUMN_PRESET:
                value = presetValue;
                break;
            case COLUMN_HAS_VALUES:
                value = hasValues;
                break;
            default:
                value = "?";
        }
        return value;
    }
