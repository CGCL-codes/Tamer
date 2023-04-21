    private void readChannel(TGTrack track) {
        int header = readHeader();
        track.getChannel().setChannel(readByte());
        track.getChannel().setEffectChannel(readByte());
        track.getChannel().setInstrument(readByte());
        track.getChannel().setVolume(readByte());
        track.getChannel().setBalance(readByte());
        track.getChannel().setChorus(readByte());
        track.getChannel().setReverb(readByte());
        track.getChannel().setPhaser(readByte());
        track.getChannel().setTremolo(readByte());
        track.setSolo((header & CHANNEL_SOLO) != 0);
        track.setMute((header & CHANNEL_MUTE) != 0);
    }
