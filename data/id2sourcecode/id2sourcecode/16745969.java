    static Channel getChannel(Interp interp, String chanName) {
        return ((Channel) getInterpChanTable(interp).get(chanName));
    }
