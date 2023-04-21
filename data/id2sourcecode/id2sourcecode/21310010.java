    @Override
    public ArrayList<Channel> getChannels() {
        ArrayList<Channel> channels = new ArrayList<Channel>();
        Object allChan[] = ChannelAPI.GetAllChannels();
        for (Object ch : allChan) {
            Channel c = new Channel();
            c.setNumber(ChannelAPI.GetChannelNumber(ch));
            if (StringUtils.isEmpty(c.getNumber())) continue;
            c.setDescription(ChannelAPI.GetChannelDescription(ch));
            c.setName(ChannelAPI.GetChannelName(ch));
            c.setNetwork(ChannelAPI.GetChannelNetwork(ch));
            c.enabled().set(ChannelAPI.IsChannelViewable(ch));
            c.setStationId(ChannelAPI.GetStationID(ch));
            channels.add(c);
        }
        Collections.sort(channels, new ChannelNumberComparator());
        return channels;
    }
