    public void setNicks(String[] nicks, String[] modes) {
        for (int i = 0; i < nicks.length; i++) _nicks.put(nicks[i].toLowerCase(java.util.Locale.ENGLISH), new Nick(nicks[i], modes[i], getIRCServer().getChannelModes(), getIRCServer().getNickModes()));
        _listeners.sendEvent("nickSet", nicks, modes, this);
    }
