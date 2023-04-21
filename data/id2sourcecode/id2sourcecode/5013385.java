    public JWondrousMachine(JWondrousMachine jwm) {
        setChannel(jwm.getChannel());
        setInstrument(jwm.getInstrument());
        setVolume(jwm.getVolume());
        setSpeed(jwm.getSpeed());
        setMinPitch(jwm.getMinPitch());
        setMaxPitch(jwm.getMaxPitch());
        setStartValue(jwm.getStartValue());
        setEndValue(jwm.getEndValue());
        setCurrentValue(jwm.getStartValue());
        setLimit(jwm.getLimit());
        setMultiplier(jwm.getMultiplier());
    }
