    @Override
    public AbstractBaseEntity getChannel(String addr) {
        ChannelEntity entity = new ChannelEntity();
        try {
            if (addr == null) {
                tmpurl = "http://" + UserBaseInfo.mURI + "/channel.php";
            } else {
                tmpurl = "http://" + UserBaseInfo.mURI + "/channel.php?address=" + URLEncoder.encode(addr, "GB2312");
            }
            entity.parseV10000(new String(sendRequest(tmpurl), "GB2312"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return entity;
    }
