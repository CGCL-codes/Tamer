    public RGBBase.Channel getTargetChannel() {
        if (targetChannel != null) {
            return targetChannel;
        } else {
            Number number = this.getNumber();
            RGBBase.Channel[] channels = number.getChannels();
            if (channels != null && channels.length == 1) {
                return channels[0];
            } else {
                return null;
            }
        }
    }
