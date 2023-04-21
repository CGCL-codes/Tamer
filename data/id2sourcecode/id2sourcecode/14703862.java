    public String getTxt() {
        ChannelTxt txt = getChannelTxt();
        if (txt != null) {
            return txt.getTxt();
        } else {
            return null;
        }
    }
