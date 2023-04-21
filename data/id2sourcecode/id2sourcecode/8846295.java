    private void addTrackValues(ABCTrack[] tracks) {
        for (int i = 0; i < tracks.length; i++) {
            TGTrack track = this.manager.getSong().getTrack(i);
            TGChannel tgChannel = this.manager.addChannel();
            tgChannel.setVolume((short) 127);
            tgChannel.setBalance((short) ((tracks[i].getPan() * 127) / 15));
            tgChannel.setProgram((short) tracks[i].getInstrument());
            tgChannel.setBank(tracks[i].isPercussion() ? TGChannel.DEFAULT_PERCUSSION_BANK : TGChannel.DEFAULT_BANK);
            track.setChannelId(tgChannel.getChannelId());
            track.setName(tracks[i].getName());
            track.getStrings().clear();
            int strings[] = tracks[i].getStrings();
            for (int j = 0; j < strings.length; j++) {
                if (j >= 7) {
                    break;
                }
                TGString string = this.manager.getFactory().newString();
                string.setNumber((j + 1));
                if (tracks[i].isPercussion()) string.setValue(0); else if (tracks[i].isBagpipe()) string.setValue(strings[j]); else string.setValue(strings[j] - 24);
                track.getStrings().add(string);
            }
        }
    }
