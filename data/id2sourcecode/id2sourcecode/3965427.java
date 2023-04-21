    public void scoreToMelodies() {
        if (score != null) {
            MIDIBeast.setResolution(resolution);
            MIDIBeast.calculateNoteTypes(score.getDenominator());
            allParts = new ArrayList<jm.music.data.Part>();
            allParts.addAll(Arrays.asList(score.getPartArray()));
            ImportMelody importMelody = new ImportMelody(score);
            melodies = new LinkedList<MidiImportRecord>();
            for (int i = 0; i < importMelody.size(); i++) {
                try {
                    jm.music.data.Part part = importMelody.getPart(i);
                    int channel = part.getChannel();
                    int numTracks = part.getSize();
                    for (int j = 0; j < numTracks; j++) {
                        MelodyPart partOut = new MelodyPart();
                        importMelody.convertToImpPart(part, j, partOut, resolution, startFactor);
                        String instrumentString = MIDIBeast.getInstrumentForPart(part);
                        if (channel != DRUM_CHANNEL) {
                            partOut.setInstrument(part.getInstrument());
                        }
                        MidiImportRecord record = new MidiImportRecord(channel, j, partOut, instrumentString);
                        melodies.add(record);
                    }
                } catch (java.lang.OutOfMemoryError e) {
                    ErrorLog.log(ErrorLog.SEVERE, "There is not enough memory to continue importing this MIDI file.");
                    return;
                }
            }
            Collections.sort(melodies);
        }
    }
