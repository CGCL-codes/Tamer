    private void addTrackValues(TETrack[] tracks) {
        for (int i = 0; i < tracks.length; i++) {
            TGTrack track = this.manager.getSong().getTrack(i);
            TGChannel tgChannel = this.manager.addChannel();
            tgChannel.setVolume((short) (((15 - tracks[i].getVolume()) * 127) / 15));
            tgChannel.setBalance((short) ((tracks[i].getPan() * 127) / 15));
            tgChannel.setProgram((short) tracks[i].getInstrument());
            tgChannel.setBank(tracks[i].isPercussion() ? TGChannel.DEFAULT_PERCUSSION_BANK : TGChannel.DEFAULT_BANK);
            track.setChannelId(tgChannel.getChannelId());
            track.getStrings().clear();
            int strings[] = tracks[i].getStrings();
            for (int j = 0; j < strings.length; j++) {
                if (j >= 7) {
                    break;
                }
                TGString string = this.manager.getFactory().newString();
                string.setNumber((j + 1));
                string.setValue((tracks[i].isPercussion() ? 0 : (96 - strings[j])));
                track.getStrings().add(string);
            }
        }
    }
