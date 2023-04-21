    public void previewChord(final TGChord chord) {
        new Thread(new Runnable() {

            public void run() {
                int playedStrings = 0;
                int stringCount = Math.min(getMaxStrings(), chord.countStrings());
                for (int i = 0; i < stringCount; i++) {
                    if (chord.getFretValue(i) != -1) {
                        playedStrings++;
                    }
                }
                int next = 0;
                int[][] beat = new int[playedStrings][2];
                for (int i = 0; i < stringCount; i++) {
                    int string = (stringCount - i);
                    int value = chord.getFretValue(string - 1);
                    if (value != -1) {
                        beat[next][0] = getCurrentTrack().getOffset() + getCurrentTrack().getString(string).getValue() + value;
                        beat[next][1] = TGVelocities.DEFAULT;
                        next++;
                    }
                }
                TGChannel channel = TuxGuitar.instance().getSongManager().getChannel(getCurrentTrack().getChannelId());
                if (channel != null) {
                    TuxGuitar.instance().getPlayer().playBeat(channel.getChannelId(), channel.getBank(), channel.getProgram(), channel.getVolume(), channel.getBalance(), channel.getChorus(), channel.getReverb(), channel.getPhaser(), channel.getTremolo(), beat, 200, 200);
                }
            }
        }).start();
    }
