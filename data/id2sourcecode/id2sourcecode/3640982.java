    protected void scoreToSeq(Score score) throws InvalidMidiDataException {
        if (seq == null) seq = new Sequence(Sequence.PPQ, m_ppqn); else empty();
        m_masterTempo = m_currentTempo = new Float(score.getTempo()).floatValue();
        Track longestTrack = null;
        double longestTime = 0.0;
        double longestRatio = 1.0;
        Enumeration parts = score.getPartList().elements();
        while (parts.hasMoreElements()) {
            Part inst = (Part) parts.nextElement();
            int currChannel = inst.getChannel();
            if (currChannel > 16) {
                InvalidMidiDataException ex = new InvalidMidiDataException(inst.getTitle() + " - Invalid Channel Number: " + currChannel);
                ex.fillInStackTrace();
                throw ex;
            }
            m_tempoHistory.push(new Float(m_currentTempo));
            float tempo = new Float(inst.getTempo()).floatValue();
            if (tempo != Part.DEFAULT_TEMPO) {
                m_currentTempo = tempo;
            } else if (tempo < Part.DEFAULT_TEMPO) System.out.println("jMusic MidiSynth error: Part TempoEvent (BPM) too low = " + tempo);
            trackTempoRatio = m_masterTempo / m_currentTempo;
            int instrument = inst.getInstrument();
            if (instrument == JMC.NO_INSTRUMENT) instrument = 0;
            Enumeration phrases = inst.getPhraseList().elements();
            double max = 0;
            double currentTime = 0.0;
            Track currTrack = seq.createTrack();
            while (phrases.hasMoreElements()) {
                Phrase phrase = (Phrase) phrases.nextElement();
                currentTime = phrase.getStartTime();
                long phraseTick = (long) (currentTime * m_ppqn * trackTempoRatio);
                MidiEvent evt;
                if (phrase.getInstrument() != JMC.NO_INSTRUMENT) instrument = phrase.getInstrument();
                evt = createProgramChangeEvent(currChannel, instrument, phraseTick);
                currTrack.add(evt);
                m_tempoHistory.push(new Float(m_currentTempo));
                tempo = new Float(phrase.getTempo()).floatValue();
                if (tempo != Phrase.DEFAULT_TEMPO) {
                    m_currentTempo = tempo;
                }
                elementTempoRatio = m_masterTempo / m_currentTempo;
                double lastPanPosition = -1.0;
                int offSetTime = 0;
                Enumeration notes = phrase.getNoteList().elements();
                while (notes.hasMoreElements()) {
                    Note note = (Note) notes.nextElement();
                    offSetTime = (int) (note.getOffset() * m_ppqn * elementTempoRatio);
                    int pitch = -1;
                    pitch = note.getPitch();
                    int dynamic = note.getDynamic();
                    if (pitch == Note.REST) {
                        phraseTick += note.getRhythmValue() * m_ppqn * elementTempoRatio;
                        continue;
                    }
                    long onTick = (long) (phraseTick);
                    if (note.getPan() != lastPanPosition) {
                        evt = createCChangeEvent(currChannel, 10, (int) (note.getPan() * 127), onTick);
                        currTrack.add(evt);
                        lastPanPosition = note.getPan();
                    }
                    evt = createNoteOnEvent(currChannel, pitch, dynamic, onTick + offSetTime);
                    currTrack.add(evt);
                    long offTick = (long) (phraseTick + note.getDuration() * m_ppqn * elementTempoRatio);
                    evt = createNoteOffEvent(currChannel, pitch, dynamic, offTick + offSetTime);
                    currTrack.add(evt);
                    phraseTick += note.getRhythmValue() * m_ppqn * elementTempoRatio;
                    if ((double) offTick > longestTime) {
                        longestTime = (double) offTick;
                        longestTrack = currTrack;
                    }
                }
                Float d = (Float) m_tempoHistory.pop();
                m_currentTempo = d.floatValue();
            }
            Float d = (Float) m_tempoHistory.pop();
            m_currentTempo = d.floatValue();
        }
        addCallBacksToSeq(longestTime);
        addEndEvent(longestTrack, longestTime);
    }
