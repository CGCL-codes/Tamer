    public static void scoreToSMF(Score score, SMF smf, boolean cofroze) {
        if (VERBOSE) System.out.println("Converting to SMF data structure...");
        double partTempoMultiplier = 1.0;
        double scoreTempo = score.getTempo();
        int phraseNumb;
        Phrase phrase1, phrase2;
        Track smfT = new Track();
        smfT.addEvent(new TempoEvent(0, score.getTempo()));
        smfT.addEvent(new TimeSig(0, score.getNumerator(), score.getDenominator()));
        smfT.addEvent(new KeySig(0, score.getKeySignature()));
        smfT.addEvent(new EndTrack());
        smf.getTrackList().addElement(smfT);
        int partCount = 0;
        Enumeration enumr = score.getPartList().elements();
        while (enumr.hasMoreElements()) {
            Track smfTrack = new Track();
            Part inst = ((Part) enumr.nextElement()).copy();
            if (cofroze) {
                if (inst.getChannel() == 0) {
                    try {
                        Exception e = new Exception("need to set cofroze to false");
                        e.fillInStackTrace();
                        throw e;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                inst.setChannel(inst.getChannel() - 1);
            }
            System.out.print("    Part " + partCount + " '" + inst.getTitle() + "' to SMF Track on Ch. " + inst.getChannel() + ": ");
            partCount++;
            if (inst.getTempo() != Part.DEFAULT_TEMPO) partTempoMultiplier = scoreTempo / inst.getTempo(); else partTempoMultiplier = 1.0;
            phraseNumb = inst.getPhraseList().size();
            for (int i = 0; i < phraseNumb; i++) {
                phrase1 = (Phrase) inst.getPhraseList().elementAt(i);
                for (int j = 0; j < phraseNumb; j++) {
                    phrase2 = (Phrase) inst.getPhraseList().elementAt(j);
                    if (phrase2.getStartTime() > phrase1.getStartTime()) {
                        inst.getPhraseList().setElementAt(phrase2, i);
                        inst.getPhraseList().setElementAt(phrase1, j);
                        break;
                    }
                }
            }
            HashMap midiEvents = new HashMap();
            if (inst.getTempo() != Part.DEFAULT_TEMPO) {
                addEvent(midiEvents, 0.0, new TempoEvent(inst.getTempo()));
            }
            if (inst.getInstrument() != NO_INSTRUMENT) {
                addEvent(midiEvents, 0.0, new PChange((short) inst.getInstrument(), (short) inst.getChannel(), 0));
            }
            if (inst.getNumerator() != NO_NUMERATOR) {
                addEvent(midiEvents, 0.0, new TimeSig(inst.getNumerator(), inst.getDenominator()));
            }
            if (inst.getKeySignature() != NO_KEY_SIGNATURE) {
                addEvent(midiEvents, 0.0, new KeySig(inst.getKeySignature(), inst.getKeyQuality()));
            }
            Enumeration partEnum = inst.getPhraseList().elements();
            double max = 0;
            double startTime = 0.0;
            double offsetValue = 0.0;
            ODouble prest = (new ODouble()).construct(-0.1);
            int phraseCounter = 0;
            double phraseTempoMultiplier = 1.0;
            while (partEnum.hasMoreElements()) {
                Phrase phrase = (Phrase) partEnum.nextElement();
                Enumeration phraseEnum = phrase.getNoteList().elements();
                startTime = phrase.getStartTime() * partTempoMultiplier;
                if (phrase.getInstrument() != NO_INSTRUMENT) {
                    addEvent(midiEvents, startTime, new PChange((short) phrase.getInstrument(), (short) inst.getChannel(), 0));
                }
                if (phrase.getTempo() != Phrase.DEFAULT_TEMPO) {
                    phraseTempoMultiplier = (scoreTempo * partTempoMultiplier) / phrase.getTempo();
                } else {
                    phraseTempoMultiplier = partTempoMultiplier;
                }
                int noteCounter = 0;
                System.out.print(" Phrase " + phraseCounter++ + ":");
                double pan = -1.0;
                resetTicker();
                while (phraseEnum.hasMoreElements()) {
                    Note note = (Note) phraseEnum.nextElement();
                    offsetValue = note.getOffset();
                    if (note.getPan() != pan) {
                        pan = note.getPan();
                        addEvent(midiEvents, startTime + offsetValue, new CChange((short) 10, (short) (pan * 127), (short) inst.getChannel(), 0));
                    }
                    int pitch = 0;
                    pitch = note.getPitch();
                    if (pitch != REST) {
                        addEvent(midiEvents, startTime + offsetValue, new NoteOn((short) pitch, (short) note.getDynamic(), (short) inst.getChannel(), 0));
                        double endTime = startTime + (note.getDuration() * phraseTempoMultiplier);
                        addEvent(midiEvents, endTime + offsetValue, new NoteOn((short) pitch, (short) 0, (short) inst.getChannel(), 0));
                    }
                    PO.p("st = " + startTime + "pitch = " + pitch);
                    startTime += tickRounder(note.getRhythmValue() * phraseTempoMultiplier);
                    System.out.print(".");
                }
            }
            Object[] keys = midiEvents.keySet().toArray();
            Arrays.sort(keys);
            double st = 0.0;
            double sortStart;
            int time;
            resetTicker();
            for (int index = 0; index < keys.length; index++) {
                Event[] evs = (Event[]) midiEvents.get(keys[index]);
                sortStart = ((Double) keys[index]).doubleValue();
                time = (int) (((((sortStart - st) * (double) smf.getPPQN())) * partTempoMultiplier) + 0.5);
                st = sortStart;
                PO.p("time = " + time);
                evs[0].setTime(time);
                smfTrack.addEvent(evs[0]);
                for (int i = 1; i < evs.length; i++) {
                    evs[i].setTime(0);
                    if (evs[i] instanceof NoteOn && ((NoteOn) evs[i]).getVelocity() > 0) PO.p("    ev " + i + " = " + ((NoteOn) evs[i]).getPitch());
                    smfTrack.addEvent(evs[i]);
                }
            }
            smfTrack.addEvent(new EndTrack());
            smf.getTrackList().addElement(smfTrack);
            System.out.println();
        }
    }
