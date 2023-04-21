    @Override
    public void appendResponse(final StringBuilder b) {
        for (ChannelStatus status : statusses) {
            b.append("\n  channel=");
            b.append(status.getChannelId());
            b.append(", output=");
            b.append(status.isOutput());
            b.append(", edit=");
            b.append(status.isEdit());
            b.append(", solo=");
            b.append(status.isSolo());
            b.append(", fadeActive=");
            b.append(status.isFadeActive());
        }
    }
