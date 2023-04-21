    private ArrayList<Channel> generateChannelList(BigInteger sum) {
        ArrayList<Channel> channels = new ArrayList<Channel>();
        ArrayList<ChannelOld> oldChannels = getOldChannels();
        for (Iterator<ChannelOld> iter = oldChannels.iterator(); iter.hasNext(); ) {
            ChannelOld oldChannel = (ChannelOld) iter.next();
            BigInteger channelId = two_pow(oldChannel.getUid());
            if (channelId.and(sum).compareTo(BigInteger.ZERO) > 0) {
                sum = sum.subtract(channelId);
                channels.add(channelManager.getChannel(oldChannel.getName()));
            }
        }
        return channels;
    }
