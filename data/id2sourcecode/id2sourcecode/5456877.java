    public static boolean[] getChannelPresence(Sequence seq) {
        boolean[] eventPresence = new boolean[16];
        Track[] tracks = seq.getTracks();
        for (int tn = 0; tn < tracks.length; tn++) {
            Track track = tracks[tn];
            for (int en = 0; en < track.size(); en++) {
                MidiEvent me = track.get(en);
                MidiMessage mm = me.getMessage();
                int status = mm.getStatus();
                int channel = status & 0x0F;
                if (mm instanceof ShortMessage) eventPresence[channel] = true;
            }
        }
        return eventPresence;
    }
