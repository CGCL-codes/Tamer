    private void updateController(TGTrack track) {
        try {
            int volume = (int) ((this.getVolume() / 10.00) * track.getChannel().getVolume());
            int balance = track.getChannel().getBalance();
            int chorus = track.getChannel().getChorus();
            int reverb = track.getChannel().getReverb();
            int phaser = track.getChannel().getPhaser();
            int tremolo = track.getChannel().getTremolo();
            updateController(track.getChannel().getChannel(), volume, balance, chorus, reverb, phaser, tremolo, 127);
            if (track.getChannel().getChannel() != track.getChannel().getEffectChannel()) {
                updateController(track.getChannel().getEffectChannel(), volume, balance, chorus, reverb, phaser, tremolo, 127);
            }
            getSequencer().setMute(track.getNumber(), track.isMute());
            getSequencer().setSolo(track.getNumber(), track.isSolo());
        } catch (MidiPlayerException e) {
            e.printStackTrace();
        }
    }
