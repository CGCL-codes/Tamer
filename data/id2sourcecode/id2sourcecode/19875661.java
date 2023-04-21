    private void channelNameChanged(final Channel changedChannel) {
        for (int i = 0; i < patch.getDimmerCount(); i++) {
            PatchDetail detail = patch.getDetail(i);
            Channel channel = detail.getDimmer().getChannel();
            if (channel == changedChannel) {
                fireTableCellUpdated(i, PatchDetail.CHANNEL_NAME);
            }
        }
    }
