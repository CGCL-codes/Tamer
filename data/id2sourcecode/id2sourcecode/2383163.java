    private static final String getSoundFormat(Infos mediaInfos) {
        LOG.debug("recuperation du format de son");
        String res = null;
        String sound = mediaInfos.getAudio().getFormat();
        int channel = mediaInfos.getAudio().getChannel();
        if ("AC-3".equals(sound)) {
            res = "DD";
        } else if ("DTS".equals(sound)) {
            res = "DTS";
        }
        if (res != null) {
            if (channel == 2 && !"DTS".equals(res)) {
                res += "20";
            } else if (channel == 6) {
                res += "51";
            } else if (channel == 8) {
                res += "71";
            } else {
                res = null;
            }
            LOG.debug("format de son : " + res);
        } else {
            LOG.info("format de son non recupere : " + sound + " | " + channel);
        }
        return res;
    }
