    public void applyACTChanAtCue(ACTAtCue n) {
        if (!n.hasSelection()) _console.at(int2short(n._level)); else _console.at(n._channels.getChannels(), n._level);
    }
