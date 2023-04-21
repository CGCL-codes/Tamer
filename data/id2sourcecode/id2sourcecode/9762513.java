    private void engaged() {
        Rank rank = getElement();
        for (int n = 0; n < played.length; n++) {
            played[n] = 0;
        }
        Channel channel = null;
        try {
            for (Sound sound : rank.getReferenced(Sound.class)) {
                SoundPlayer<?> player = (SoundPlayer<?>) getPlayer(sound);
                if (player != null) {
                    channel = player.createChannel(new RankChannelFilter(rank.getChannel()));
                    break;
                }
            }
        } catch (ProcessingException ex) {
            channel = new DeadChannel();
            addProblem(Severity.ERROR, "channel", "channelIllegal", rank.getChannel());
            return;
        }
        if (rank.getDelay() > 0) {
            channel = new DelayedChannel(channel);
        }
        if (channel == null) {
            channel = new DeadChannel();
            addProblem(Severity.WARNING, "channel", "channelUnavailable", rank.getChannel());
        }
        for (Element element : rank.getReferenced(Element.class)) {
            if (element instanceof SoundFilter) {
                FilterPlayer player = (FilterPlayer) getPlayer(element);
                channel = player.filter(channel);
            }
        }
        this.channel = new ChannelImpl(channel);
        this.channel.init();
    }
