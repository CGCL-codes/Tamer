    private void createMIDIAndMPMappingAndWAVs(Score score, File dir, PrintStream progressLogStream) throws IOException {
        progressLogStream.println("Write Midi file...");
        SequenceContainer sc = MidiConverter.convertToSequence(score, true, false, fr(1, 1));
        int[] types = MidiSystem.getMidiFileTypes(sc.sequence);
        int type = 0;
        if (types.length != 0) {
            type = types[types.length - 1];
        }
        MidiSystem.write(sc.sequence, type, new File(dir, "midi.mid"));
        progressLogStream.println("Write MPs map...");
        Document xmlDoc = createEmptyDocument();
        Element eRoot = addElement("midi", xmlDoc);
        Element eMPs = addElement("mps", eRoot);
        for (MidiTime time : sc.timePool) {
            Element eSystem = addElement("mp", eMPs);
            addAttribute(eSystem, "tick", time.tick);
            addAttribute(eSystem, "measure", time.mp.getMeasure());
            addAttribute(eSystem, "beat", time.mp.getBeat());
        }
        XMLWriter.writeFile(xmlDoc, new FileOutputStream(new File(dir, "midi.xml")));
        Pino10Map pino10Map = null;
        String[] args = CommandLine.getArgs();
        for (int i = 0; args != null && i < args.length - 1; i++) {
            if (args[i].equals("--pino10map")) {
                progressLogStream.println("Reading pino10map...");
                File pino10MapFile = new File(args[i + 1]);
                pino10Map = new Pino10Map(pino10MapFile);
                progressLogStream.println("Copy pino10map...");
                FileUtils.copyFile(pino10MapFile.getAbsolutePath(), new File(dir, pino10MapFile.getName()).getAbsolutePath());
                break;
            }
        }
        progressLogStream.println("Synthesize and write WAV files...");
        writeProgress(progressLogStream, 0, false);
        try {
            int[] lengths = new int[] { 3, 6, 8 };
            for (int articulation : range(0, 2)) {
                sc = MidiConverter.convertToSequence(score, false, false, fr(lengths[articulation], 8));
                int tracksCount = sc.sequence.getTracks().length;
                String art = (articulation == 0 ? "staccato" : articulation == 1 ? "normal" : "legato");
                MidiToWaveRenderer.render(SynthManager.getSoundbank(), sc.sequence, null, new File(dir, "all-" + art + ".wav"));
                if (pino10Map != null) {
                    int trackPos = 1;
                    int partsCount = pino10Map.partsStavesCount.size();
                    for (int iPart = 0; iPart < partsCount; iPart++) {
                        int stavesCount = pino10Map.partsStavesCount.get(iPart);
                        Set<Integer> tracks = set(0);
                        for (int i = 0; i < stavesCount; i++) tracks.add(trackPos + i);
                        trackPos += stavesCount;
                        MidiToWaveRenderer.render(SynthManager.getSoundbank(), sc.sequence, tracks, new File(dir, "part-" + iPart + "-" + art + ".wav"));
                        writeProgress(progressLogStream, 1f * (articulation * partsCount + iPart + 1) / (partsCount * 3), false);
                    }
                } else {
                    for (int iTrack = 1; iTrack < tracksCount; iTrack++) {
                        Set<Integer> tracks = set(0, iTrack);
                        MidiToWaveRenderer.render(SynthManager.getSoundbank(), sc.sequence, tracks, new File(dir, "part-" + iTrack + "-" + art + ".wav"));
                        writeProgress(progressLogStream, 1f * (articulation * tracksCount + iTrack + 1) / (tracksCount * 3), false);
                    }
                }
            }
            writeProgress(progressLogStream, 1, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.log(LogLevel.Warning, ex);
        }
    }
