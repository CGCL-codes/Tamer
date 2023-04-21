    private void toggle(final int channelIndex) {
        BooleanHolder holder = getPreHeating().getChannelEnabled(channelIndex);
        holder.setValue(!holder.getValue());
    }
