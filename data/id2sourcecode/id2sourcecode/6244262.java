    private void checkChannels() {
        for (int tc = 0; tc < this.tempChannels.size(); tc++) {
            TempChannel tempChannel = (TempChannel) this.tempChannels.get(tc);
            if (tempChannel.getTrack() > 0) {
                boolean channelExists = false;
                for (int c = 0; c < this.channels.size(); c++) {
                    TGChannel tgChannel = (TGChannel) this.channels.get(c);
                    GMChannelRoute gmChannelRoute = this.channelRouter.getRoute(tgChannel.getChannelId());
                    if (gmChannelRoute != null) {
                        if (gmChannelRoute.getChannel1() == tempChannel.getChannel() || gmChannelRoute.getChannel2() == tempChannel.getChannel()) {
                            channelExists = true;
                        }
                    }
                }
                if (!channelExists) {
                    TGChannel tgChannel = this.factory.newChannel();
                    tgChannel.setChannelId(this.channels.size() + 1);
                    tgChannel.setProgram((short) tempChannel.getInstrument());
                    tgChannel.setVolume((short) tempChannel.getVolume());
                    tgChannel.setBalance((short) tempChannel.getBalance());
                    tgChannel.setName(("#" + tgChannel.getChannelId()));
                    tgChannel.setBank(tempChannel.getChannel() == 9 ? TGChannel.DEFAULT_PERCUSSION_BANK : TGChannel.DEFAULT_BANK);
                    GMChannelRoute gmChannelRoute = new GMChannelRoute(tgChannel.getChannelId());
                    gmChannelRoute.setChannel1(tempChannel.getChannel());
                    gmChannelRoute.setChannel2(tempChannel.getChannel());
                    for (int tcAux = (tc + 1); tcAux < this.tempChannels.size(); tcAux++) {
                        TempChannel tempChannelAux = (TempChannel) this.tempChannels.get(tcAux);
                        if (tempChannel.getTrack() == tempChannelAux.getTrack()) {
                            if (gmChannelRoute.getChannel2() == gmChannelRoute.getChannel1()) {
                                gmChannelRoute.setChannel2(tempChannelAux.getChannel());
                            } else {
                                tempChannelAux.setTrack(-1);
                            }
                        }
                    }
                    this.channelRouter.configureRoutes(gmChannelRoute, (tempChannel.getChannel() == 9));
                    this.channels.add(tgChannel);
                }
            }
        }
    }
