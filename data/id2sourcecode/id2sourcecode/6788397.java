    public AfterCheckEnum getAfterCheckEnum() {
        Byte after = getChannelExt().getAfterCheck();
        Channel channel = getParent();
        while (after == null && channel != null) {
            after = channel.getAfterCheck();
            channel = channel.getParent();
        }
        if (after == null) {
            after = getSite().getAfterCheck();
        }
        if (after == 1) {
            return AfterCheckEnum.CANNOT_UPDATE;
        } else if (after == 2) {
            return AfterCheckEnum.BACK_UPDATE;
        } else if (after == 3) {
            return AfterCheckEnum.KEEP_UPDATE;
        } else {
            return AfterCheckEnum.CANNOT_UPDATE;
        }
    }
