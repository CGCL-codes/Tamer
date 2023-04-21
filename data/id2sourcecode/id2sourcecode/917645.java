    private void addConditions() {
        ChannelInfo[] cInfos = sensor.getSensorInfo().getChannelInfos();
        for (int i = 0, l = cInfos.length; i < l; i++) {
            Channel channel = sensor.getChannel(cInfos[i]);
            channel.addCondition(this, COND1);
            channel.addCondition(this, COND2);
        }
    }
