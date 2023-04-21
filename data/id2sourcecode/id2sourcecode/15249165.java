    @Override
    public AbstractBaseEntity getChannel(String addr) {
        if (addr == null) {
            tmpurl = "http://" + UserBaseInfo.mURI + "/SDK/SDK_Channel.php";
        } else {
            String encodeParam;
            try {
                encodeParam = Base64.encodeToString(addr.getBytes("GB2312"), Base64.NO_WRAP);
                tmpurl = "http://" + UserBaseInfo.mURI + "/SDK/SDK_Channel.php?" + encodeParam;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ChannelEntity entity = new ChannelEntity();
        try {
            entity.parseV10001(new String(sendRequest(tmpurl), "GB2312"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return entity;
    }
