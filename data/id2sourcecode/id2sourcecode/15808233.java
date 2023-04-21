    private void readChannel(TGSong song, TGTrack track) {
        TGChannel channel = this.factory.newChannel();
        TGChannelParameter gmChannel1Param = this.factory.newChannelParameter();
        TGChannelParameter gmChannel2Param = this.factory.newChannelParameter();
        int channel1 = readShort();
        gmChannel1Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_1);
        gmChannel1Param.setValue(Integer.toString(channel1));
        int channel2 = readShort();
        gmChannel2Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_2);
        gmChannel2Param.setValue(Integer.toString(channel2));
        channel.setBank(channel1 == 9 ? TGChannel.DEFAULT_PERCUSSION_BANK : TGChannel.DEFAULT_BANK);
        channel.setProgram(readShort());
        channel.setVolume(readShort());
        channel.setBalance(readShort());
        channel.setChorus(readShort());
        channel.setReverb(readShort());
        channel.setPhaser(readShort());
        channel.setTremolo(readShort());
        for (int i = 0; i < song.countChannels(); i++) {
            TGChannel channelAux = song.getChannel(i);
            for (int n = 0; n < channelAux.countParameters(); n++) {
                TGChannelParameter channelParameter = channelAux.getParameter(n);
                if (channelParameter.getKey().equals(GMChannelRoute.PARAMETER_GM_CHANNEL_1)) {
                    if (Integer.toString(channel1).equals(channelParameter.getValue())) {
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
