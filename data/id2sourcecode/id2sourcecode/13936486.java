    private void parseTracks(TGSong tgSong) {
        List tracks = this.document.getTracks();
        for (int i = 0; i < tracks.size(); i++) {
            GPXTrack gpTrack = (GPXTrack) this.document.getTracks().get(i);
            TGChannel tgChannel = this.factory.newChannel();
            tgChannel.setProgram((short) gpTrack.getGmProgram());
            tgChannel.setBank(gpTrack.getGmChannel1() == 9 ? TGChannel.DEFAULT_PERCUSSION_BANK : TGChannel.DEFAULT_BANK);
            TGChannelParameter gmChannel1Param = this.factory.newChannelParameter();
            gmChannel1Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_1);
            gmChannel1Param.setValue(Integer.toString(gpTrack.getGmChannel1()));
            TGChannelParameter gmChannel2Param = this.factory.newChannelParameter();
            gmChannel2Param.setKey(GMChannelRoute.PARAMETER_GM_CHANNEL_2);
            gmChannel2Param.setValue(Integer.toString(gpTrack.getGmChannel1() != 9 ? gpTrack.getGmChannel2() : gpTrack.getGmChannel1()));
            for (int c = 0; c < tgSong.countChannels(); c++) {
                TGChannel tgChannelAux = tgSong.getChannel(c);
                for (int n = 0; n < tgChannelAux.countParameters(); n++) {
                    TGChannelParameter channelParameter = tgChannelAux.getParameter(n);
                    if (channelParameter.getKey().equals(GMChannelRoute.PARAMETER_GM_CHANNEL_1)) {
                        if (Integer.toString(gpTrack.getGmChannel1()).equals(channelParameter.getValue())) {
                            tgChannel.setChannelId(tgChannelAux.getChannelId());
                        }
                    }
                }
            }
            if (tgChannel.getChannelId() <= 0) {
                tgChannel.setChannelId(tgSong.countChannels() + 1);
                tgChannel.setName(("#" + tgChannel.getChannelId()));
                tgChannel.addParameter(gmChannel1Param);
                tgChannel.addParameter(gmChannel2Param);
                tgSong.addChannel(tgChannel);
            }
            TGTrack tgTrack = this.factory.newTrack();
            tgTrack.setNumber(i + 1);
            tgTrack.setName(gpTrack.getName());
            tgTrack.setChannelId(tgChannel.getChannelId());
            if (gpTrack.getTunningPitches() != null) {
                for (int s = 1; s <= gpTrack.getTunningPitches().length; s++) {
                    TGString tgString = this.factory.newString();
                    tgString.setNumber(s);
                    tgString.setValue(gpTrack.getTunningPitches()[gpTrack.getTunningPitches().length - s]);
                    tgTrack.getStrings().add(tgString);
                }
            } else if (tgChannel.isPercussionChannel()) {
                for (int s = 1; s <= 6; s++) {
                    tgTrack.getStrings().add(TGSongManager.newString(this.factory, s, 0));
                }
            } else {
                tgTrack.getStrings().add(TGSongManager.newString(this.factory, 1, 64));
                tgTrack.getStrings().add(TGSongManager.newString(this.factory, 2, 59));
                tgTrack.getStrings().add(TGSongManager.newString(this.factory, 3, 55));
                tgTrack.getStrings().add(TGSongManager.newString(this.factory, 4, 50));
                tgTrack.getStrings().add(TGSongManager.newString(this.factory, 5, 45));
                tgTrack.getStrings().add(TGSongManager.newString(this.factory, 6, 40));
            }
            if (gpTrack.getColor() != null && gpTrack.getColor().length == 3) {
                tgTrack.getColor().setR(gpTrack.getColor()[0]);
                tgTrack.getColor().setG(gpTrack.getColor()[1]);
                tgTrack.getColor().setB(gpTrack.getColor()[2]);
            }
            tgSong.addTrack(tgTrack);
        }
    }
