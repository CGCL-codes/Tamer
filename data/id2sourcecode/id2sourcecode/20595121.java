    public void updatePrograms() {
        try {
            Iterator it = this.songManager.getSong().getTracks();
            while (it.hasNext()) {
                TGTrack track = (TGTrack) it.next();
                getOutputTransmitter().sendProgramChange(track.getChannel().getChannel(), track.getChannel().getInstrument());
                if (track.getChannel().getChannel() != track.getChannel().getEffectChannel()) {
                    getOutputTransmitter().sendProgramChange(track.getChannel().getEffectChannel(), track.getChannel().getInstrument());
                }
            }
        } catch (MidiPlayerException e) {
            e.printStackTrace();
        }
    }
