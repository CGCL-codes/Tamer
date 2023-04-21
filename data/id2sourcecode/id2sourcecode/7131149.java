    public ConsoleCue translateLevelsToChan(short[] d) {
        ConsoleCue cue = new CameoCue();
        for (int x = 0; x < _totalDimmers && x < d.length; x++) {
            int chan = getChannelFor(x);
            if (chan > -1 && d[x] > 0) cue.setLevel(chan, d[x]);
        }
        return cue;
    }
