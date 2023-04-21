    public String getClearStatements(int nchnls) {
        StrBuilder buffer = new StrBuilder();
        for (int i = 0; i < channels.size(); i++) {
            Channel c = channels.getChannel(i);
            for (int j = 0; j < nchnls; j++) {
                buffer.append(getChannelVar(c.getName(), j)).append(" = 0\n");
            }
        }
        for (int i = 0; i < subChannels.size(); i++) {
            Channel c = subChannels.getChannel(i);
            for (int j = 0; j < nchnls; j++) {
                buffer.append(getSubChannelVar(c.getName(), j)).append(" = 0\n");
            }
        }
        for (int j = 0; j < nchnls; j++) {
            buffer.append(getSubChannelVar(MASTER_CHANNEL, j)).append(" = 0\n");
        }
        return buffer.toString();
    }
