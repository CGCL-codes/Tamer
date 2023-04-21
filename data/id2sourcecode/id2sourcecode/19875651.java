    public Object getValueAt(final int row, final int col) {
        Object value;
        PatchDetail detail = patch.getDetail(row);
        switch(col) {
            case PatchDetail.ON:
                value = detail.isOn();
                break;
            case PatchDetail.DIMMER_NUMBER:
                value = detail.getDimmer().getId() + 1;
                break;
            case PatchDetail.DIMMER_NAME:
                value = detail.getDimmer().getName();
                break;
            case PatchDetail.LANBOX_CHANNEL_NUMBER:
                int channelId = detail.getDimmer().getLanboxChannelId();
                value = channelId + 1;
                break;
            case PatchDetail.CHANNEL_NUMBER:
                Channel channel = detail.getDimmer().getChannel();
                if (channel == null) {
                    value = "";
                } else {
                    value = channel.getId() + 1;
                }
                break;
            case PatchDetail.CHANNEL_NAME:
                channel = detail.getDimmer().getChannel();
                if (channel == null) {
                    value = "";
                } else {
                    value = channel.getName();
                }
                break;
            default:
                value = "?";
        }
        return value;
    }
