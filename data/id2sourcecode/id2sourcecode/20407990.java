    private void readChannel(TGSong song, TGTrack track, List channels) throws IOException {
        int gmChannel1 = (readInt() - 1);
        int gmChannel2 = (readInt() - 1);
        if (gmChannel1 >= 0 && gmChannel1 < channels.size()) {
            TGChannel channel = getFactory().newChannel();
            TGChannelParameter gmChannel1Param = getFactory().newChannelParameter();
            TGChannelParameter gmChannel2Param = getFactory().newChannelParameter();
            gmChannel1Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_1);
            gmChannel1Param.setValue(Integer.toString(gmChannel1));
            gmChannel2Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_2);
            gmChannel2Param.setValue(Integer.toString(gmChannel1 != 9 ? gmChannel2 : gmChannel1));
            ((TGChannel) channels.get(gmChannel1)).copy(channel);
            for (int i = 0; i < song.countChannels(); i++) {
                TGChannel channelAux = song.getChannel(i);
                for (int n = 0; n < channelAux.countParameters(); n++) {
                    TGChannelParameter channelParameter = channelAux.getParameter(n);
                    if (channelParameter.getKey().equals(GMChannelRoute.PARAMETER_GM_CHANNEL_1)) {
                        if (Integer.toString(gmChannel1).equals(channelParameter.getValue())) {
                            channel.setChannelId(channelAux.getChannelId());
                        }
                    }
                }
            }
            if (channel.getChannelId() <= 0) {
                channel.setChannelId(song.countChannels() + 1);
                channel.setName(("#" + channel.getChannelId()));
                channel.addParameter(gmChannel1Param);
                channel.addParameter(gmChannel2Param);
                song.addChannel(channel);
            }
            track.setChannelId(channel.getChannelId());
        }
    }
