    private void addSongDefinitions(TGSong song) {
        String[] line = { "X:1" };
        if (song.getComments() != null) line = song.getComments().split("\\n+");
        String s = "X:1";
        for (int i = 0; i < line.length; i++) {
            if (line[i].startsWith("X:")) {
                s = line[i];
                break;
            }
        }
        this.writer.println(s);
        this.writer.println("%%exprabove 0");
        this.writer.println("%%exprbelow 1");
        this.diagramt = this.settings.getDiagramTrack();
        this.chordt = this.settings.getChordTrack();
        this.baset = this.settings.getBaseTrack();
        this.dronet = this.settings.getDroneTrack();
        if (this.diagramt == ABCSettings.AUTO_TRACK || this.chordt == ABCSettings.AUTO_TRACK || this.baset == ABCSettings.AUTO_TRACK || this.dronet == ABCSettings.AUTO_TRACK) {
            for (int t = 0; t < song.countTracks(); t++) {
                TGTrack track = song.getTrack(t);
                int tnum = track.getNumber();
                if (this.settings.getTrack() == ABCSettings.ALL_TRACKS || this.settings.getTrack() == tnum) {
                    String id = track.getName();
                    if ((tnum == this.diagramt + 1 || id.indexOf('\t') < 0) && !isPercussionTrack(track)) {
                        for (int j = 0; j < song.countTracks(); j++) {
                            TGTrack buddy = song.getTrack(j);
                            if (this.chordt == j + 1 || buddy.getName().matches(id + "\tchords")) {
                                if (this.chordt == ABCSettings.AUTO_TRACK) this.chordt = j + 1;
                                if (this.diagramt == ABCSettings.AUTO_TRACK) this.diagramt = t + 1;
                            }
                            if (this.baset == j + 1 || buddy.getName().matches(id + "\tbase")) {
                                if (this.baset == ABCSettings.AUTO_TRACK) this.baset = j + 1;
                                if (this.diagramt == ABCSettings.AUTO_TRACK) this.diagramt = t + 1;
                            }
                            if (this.dronet == j + 1 || buddy.getName().matches(id + "\tdrone")) {
                                if (this.dronet == ABCSettings.AUTO_TRACK) this.dronet = j + 1;
                            }
                        }
                    }
                }
            }
        }
        if (this.chordt == ABCSettings.AUTO_TRACK) this.chordt = this.baset;
        if (this.baset == ABCSettings.AUTO_TRACK) this.baset = this.chordt;
        if (this.chordt == ABCSettings.NO_TRACK) this.baset = ABCSettings.NO_TRACK;
        if (this.baset == ABCSettings.NO_TRACK) this.chordt = ABCSettings.NO_TRACK;
        this.measures = song.countMeasureHeaders();
        this.from = ABCSettings.FIRST_MEASURE;
        this.to = ABCSettings.LAST_MEASURE;
        if (this.settings.getMeasureFrom() != ABCSettings.FIRST_MEASURE || this.settings.getMeasureTo() != ABCSettings.LAST_MEASURE) {
            this.from = this.settings.getMeasureFrom();
            to = this.settings.getMeasureTo();
        }
        if (this.from == ABCSettings.FIRST_MEASURE) this.from = 0;
        if (this.to == ABCSettings.LAST_MEASURE) to = measures - 1;
        if (this.from > measures - 1) this.from = measures - 1;
        if (this.to < this.from) this.to = this.from;
        this.measures = this.to - this.from + 1;
        this.barsperstaff = this.settings.getMeasuresPerLine();
        if (this.barsperstaff == ABCSettings.AUTO_MEASURES) {
            this.barsperstaff = 5;
            while ((measures % this.barsperstaff) > 0) --this.barsperstaff;
            if (this.barsperstaff < 2) {
                if ((measures % 3) < 2) this.barsperstaff = 3; else this.barsperstaff = 4;
            }
        }
        this.writer.println("%%%barsperstaff " + this.barsperstaff);
        if (song.getName() != null && song.getName().trim().length() > 0) this.writer.println("T:" + song.getName());
        if (song.getAlbum() != null && song.getAlbum().trim().length() > 0) this.writer.println("O:" + song.getAlbum());
        if (song.getAuthor() != null && song.getAuthor().trim().length() > 0) this.writer.println("A:" + song.getAuthor());
        if (song.getTranscriber() != null && song.getTranscriber().trim().length() > 0) this.writer.println("Z:" + song.getTranscriber());
        TGTimeSignature ts = song.getMeasureHeader(0).getTimeSignature();
        int n = ts.getNumerator();
        int d = ts.getDenominator().getValue();
        if (n == 4 && d == 4) this.writer.println("M:C"); else if (n == 2 && d == 2) this.writer.println("M:C|"); else this.writer.println("M:" + n + "/" + d);
        this.writer.println("L:1/8");
        n = song.getMeasureHeader(0).getTempo().getValue();
        this.writer.println("Q:1/4=" + n);
        int trackCount = 0;
        for (int i = 0; i < song.countTracks(); i++) {
            TGTrack track = song.getTrack(i);
            String id = handleId(track.getName());
            if (id.indexOf('\t') < 0 && !isPercussionTrack(track) && i != this.chordt - 1 && i != this.baset - 1 && i != this.dronet - 1) ++trackCount;
        }
        if (trackCount > 1 && this.settings.getTrack() == ABCSettings.ALL_TRACKS) {
            for (int i = 0; i < song.countTracks(); i++) {
                TGTrack track = song.getTrack(i);
                String id = handleId(track.getName());
                if (id.indexOf('\t') < 0 && !isPercussionTrack(track) && i != this.chordt - 1 && i != this.baset - 1 && i != this.dronet - 1) this.writer.println("V:" + id.replaceAll("\\s+", "_") + " clef=" + clefname(track.getMeasure(0).getClef()));
            }
            if (this.settings.isTrackGroupEnabled()) {
                String staves = "%%staves ";
                String stave2 = "";
                String stave3 = "";
                int v = 0;
                for (int i = 0; i < song.countTracks(); i++) {
                    TGTrack track = song.getTrack(i);
                    String id = handleId(track.getName());
                    int clef = track.getMeasure(0).getClef();
                    if (id.indexOf('\t') < 0 && !isPercussionTrack(track) && i != this.chordt - 1 && i != this.baset - 1 && i != this.dronet - 1) {
                        ++v;
                        if (v == 1 && clef == TGMeasure.CLEF_TREBLE) staves += "1 "; else if (clef == TGMeasure.CLEF_TREBLE) stave2 += v + " "; else if (clef == TGMeasure.CLEF_BASS) stave3 += v + " "; else staves += v + " ";
                    }
                }
                if (stave2.trim().split(" ").length > 1) stave2 = "(" + stave2.trim() + ")";
                if (stave3.trim().split(" ").length > 1) stave3 = "(" + stave3.trim() + ")";
                if (stave2.length() > 0 && stave3.length() > 0) staves += "{" + stave2.trim() + " " + stave3.trim() + "}"; else staves += (stave2 + stave3).trim();
                this.writer.println(staves);
            }
        }
        if (song.getMeasureHeader(0).getTripletFeel() != TGMeasureHeader.TRIPLET_FEEL_NONE) this.writer.println("R:hornpipe");
        this.writer.println("K:" + ABC_KEY_SIGNATURES[song.getTrack(0).getMeasure(0).getKeySignature()]);
        int v = 0;
        TGBeat[] gchordBeat = new TGBeat[2];
        for (int i = 0; i < song.countTracks(); i++) {
            TGTrack track = song.getTrack(i);
            String id = handleId(track.getName());
            int tnum = track.getNumber();
            if (id.indexOf('\t') > 0) {
                for (int j = 0; j < song.countTracks(); j++) {
                    TGTrack buddy = song.getTrack(j);
                    if (id.matches(buddy.getName() + "\t.*")) {
                        tnum = buddy.getNumber();
                        break;
                    }
                }
            }
            if (this.settings.getTrack() == ABCSettings.ALL_TRACKS || this.settings.getTrack() == tnum) {
                TGChannel channel = getChannel(track);
                int instrument = channel.getProgram() + this.instrumentoffset;
                String instrName = MidiInstrument.INSTRUMENT_LIST[instrument - this.instrumentoffset].getName();
                int pan = channel.getBalance();
                if (isPercussionTrack(track)) {
                    TGBeat beat = getFirstBeat(track);
                    if (beat == null) {
                        this.writer.println("%%MIDI drumoff");
                        this.drum = "";
                    } else {
                        this.writer.println("%%MIDI drumon");
                        this.drum = getABCDrum(track, beat.getMeasure().getNumber());
                        this.writer.println("%%MIDI drum " + this.drum);
                    }
                } else if (id.indexOf('\t') < 0 && this.chordt != i + 1 && this.baset != i + 1 && this.dronet != i + 1) {
                    ++v;
                    this.writer.println("%%MIDI voice " + id.replaceAll("\\s+", "_") + " instrument=" + instrument + " % " + instrName + " pan=" + pan);
                } else if (this.dronet == i + 1 || id.endsWith("\tdrone")) {
                    TGBeat beat = getFirstBeat(track);
                    if (beat != null) {
                        this.droneparm = getDroneParm(beat);
                        this.droneparm[4] = instrument;
                        String drone = instrument + "        " + droneparm[0] + "   " + droneparm[1] + "   " + droneparm[2] + "   " + droneparm[3];
                        this.writer.println("%      drone <instrument> <pitch1> <pitch2> <vol1> <vol2>");
                        this.writer.println("%%MIDI drone " + drone + " % " + instrName);
                        this.droneon = beat.getMeasure().getNumber() < 2;
                        if (this.droneon) this.writer.println("%%MIDI droneon"); else this.writer.println("%%MIDI droneoff");
                    }
                } else if (this.chordt == i + 1 || id.endsWith("\tchords")) {
                    gchordBeat[0] = getFirstBeat(track);
                    if (gchordBeat[0] != null) {
                        int[] p = getDroneParm(gchordBeat[0]);
                        this.writer.println("%%MIDI chordprog " + instrument + " % " + instrName);
                        this.writer.println("%%MIDI chordvol " + p[2]);
                    }
                } else if (this.baset == i + 1 || id.endsWith("\tbase")) {
                    gchordBeat[1] = getFirstBeat(track);
                    if (gchordBeat[1] != null) {
                        int[] p = getDroneParm(gchordBeat[1]);
                        this.writer.println("%%MIDI bassprog " + instrument + " % " + instrName);
                        this.writer.println("%%MIDI bassvol " + p[2]);
                    }
                }
            }
        }
        s = null;
        v = -1;
        boolean gchords = false;
        for (int i = 0; i < 2; i++) {
            if (gchordBeat[i] != null) {
                gchords = true;
                if (v < 0) v = i; else if (gchordBeat[i].getMeasure().getNumber() < gchordBeat[v].getMeasure().getNumber()) v = i;
                if (gchordBeat[i].getMeasure().getNumber() < 2) s = "%%MIDI gchordon";
            }
        }
        if (s == null && gchords) s = "%%MIDI gchordoff";
        this.gchord = null;
        if (v >= 0) {
            this.gchord = getABCGchord(song, gchordBeat[v].getMeasure().getNumber());
        }
        if (this.gchord != null) this.writer.println("%%MIDI gchord " + this.gchord);
        if (s != null) this.writer.println(s);
    }
