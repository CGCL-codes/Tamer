    private void updateChannelCombos(TGChannel tgChannel) {
        GMChannelRoute route = this.router.getRoute(tgChannel.getChannelId());
        List channels = this.router.getFreeChannels(route);
        String channel1Prefix = TuxGuitar.getProperty("instrument.channel");
        String channel2Prefix = TuxGuitar.getProperty("instrument.effect-channel");
        this.reloadChannelCombo(this.gmChannel1Combo, channels, route.getChannel1(), channel1Prefix);
        this.reloadChannelCombo(this.gmChannel2Combo, channels, route.getChannel2(), channel2Prefix);
        boolean playerRunning = TuxGuitar.instance().getPlayer().isRunning();
        this.gmChannel1Combo.setEnabled(!playerRunning && !tgChannel.isPercussionChannel() && this.gmChannel1Combo.getItemCount() > 0);
        this.gmChannel2Combo.setEnabled(!playerRunning && !tgChannel.isPercussionChannel() && this.gmChannel2Combo.getItemCount() > 0);
    }
