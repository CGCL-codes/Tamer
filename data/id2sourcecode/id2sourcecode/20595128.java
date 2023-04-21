    public void playBeat(final TGTrack track, final List notes) {
        int channel = track.getChannel().getChannel();
        int program = track.getChannel().getInstrument();
        int volume = (int) ((this.getVolume() / 10.00) * track.getChannel().getVolume());
        int balance = track.getChannel().getBalance();
        int chorus = track.getChannel().getChorus();
        int reverb = track.getChannel().getReverb();
        int phaser = track.getChannel().getPhaser();
        int tremolo = track.getChannel().getTremolo();
        int size = notes.size();
        int[][] beat = new int[size][2];
        for (int i = 0; i < size; i++) {
            TGNote note = (TGNote) notes.get(i);
            beat[i][0] = track.getOffset() + (note.getValue() + ((TGString) track.getStrings().get(note.getString() - 1)).getValue());
            beat[i][1] = note.getVelocity();
        }
        playBeat(channel, program, volume, balance, chorus, reverb, phaser, tremolo, beat);
    }
