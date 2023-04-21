    public void copy(TGChannel channel) {
        channel.setChannel(getChannel());
        channel.setEffectChannel(getEffectChannel());
        channel.setInstrument(getInstrument());
        channel.setVolume(getVolume());
        channel.setBalance(getBalance());
        channel.setChorus(getChorus());
        channel.setReverb(getReverb());
        channel.setPhaser(getPhaser());
        channel.setTremolo(getTremolo());
    }
