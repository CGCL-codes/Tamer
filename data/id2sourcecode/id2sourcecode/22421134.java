    public void requestFieldSetting(final double field) {
        try {
            _fieldWriteChannelWrapper.getChannel().putValCallback(field, PUT_HANDLER);
        } catch (Exception exception) {
            throw new RuntimeException("Field setting request exception.", exception);
        }
    }
