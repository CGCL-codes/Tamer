    public final void getInfo() {
        try {
            this.channel = getChannelInfo();
            this.user = getUserInfo();
        } catch (Exception e) {
            this.channel = null;
            this.user = null;
        }
    }
