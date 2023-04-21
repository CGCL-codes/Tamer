    private void setValueChannelAt(final Object value, final int channelIndex, final int col) {
        if (col == COLUMN_NAME) {
            getShow().getChannels().get(channelIndex).setName((String) value);
        } else if (col == COLUMN_SUBMASTER) {
            if (selectedSubmasterIndex != -1) {
                LightCues lightCues = getShow().getCues().getLightCues();
                String stringValue = (String) value;
                stringValue = stringValue.trim();
                if ("-".equals(stringValue)) {
                    lightCues.deactivateSubmasterLevel(selectedSubmasterIndex, channelIndex);
                } else {
                    float floatValue = 0f;
                    if (stringValue.length() > 0) {
                        int intValue = Util.toInt(stringValue);
                        floatValue = (((float) intValue) / 100) + 0.001f;
                    }
                    lightCues.setSubmasterChannel(selectedSubmasterIndex, channelIndex, floatValue);
                }
            }
        }
    }
