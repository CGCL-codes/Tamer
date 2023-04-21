    protected int[][] getChannelRoutingSettings() {
        int[][] channelsRoute = new int[16][];
        for (int i = 0; i < channelsRoute.length; i++) {
            channelsRoute[i] = new int[4];
            channelsRoute[i][0] = i;
            channelsRoute[i][1] = this.settings.getConfig().getIntConfigValue("jack.midi.port.channel-routing.to-channel-" + i, -1);
            channelsRoute[i][2] = this.settings.getConfig().getIntConfigValue("jack.midi.port.channel-routing.to-program-" + i, -1);
            channelsRoute[i][3] = this.settings.getConfig().getIntConfigValue("jack.midi.port.channel-routing.to-bank-" + i, -1);
        }
        return channelsRoute;
    }
