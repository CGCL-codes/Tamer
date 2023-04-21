    public Programme getNowProgramme(String channelName, Calendar t) {
        Channel chan = getChannel(channelName);
        Programme prog = chan.getByTime(t);
        return prog;
    }
