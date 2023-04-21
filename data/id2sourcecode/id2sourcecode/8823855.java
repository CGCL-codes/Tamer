    private String getChannelString() {
        switch(this.raw.getByte(0)) {
            case jelb.netio.Protocol.CHAT_CHANNEL1:
                return "@1";
            case jelb.netio.Protocol.CHAT_CHANNEL2:
                return "@2";
            case jelb.netio.Protocol.CHAT_CHANNEL3:
                return "@3";
            case jelb.netio.Protocol.CHAT_GM:
                return "#gm";
            case jelb.netio.Protocol.CHAT_MOD:
                return "#mod";
        }
        return "";
    }
