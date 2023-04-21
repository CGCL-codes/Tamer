    private void makeNotes(MidiSequenceHandler sequence, TGTrack track, TGBeat beat, TGTempo tempo, int measureIdx, int bIndex, long startMove, int[] stroke) {
        int trackId = track.getNumber();
        for (int vIndex = 0; vIndex < beat.countVoices(); vIndex++) {
            TGVoice voice = beat.getVoice(vIndex);
            BeatData data = checkTripletFeel(voice, bIndex);
            for (int noteIdx = 0; noteIdx < voice.countNotes(); noteIdx++) {
                TGNote note = voice.getNote(noteIdx);
                if (!note.isTiedNote()) {
                    int key = (this.transpose + track.getOffset() + note.getValue() + ((TGString) track.getStrings().get(note.getString() - 1)).getValue());
                    long start = applyStrokeStart(note, (data.getStart() + startMove), stroke);
                    long duration = applyStrokeDuration(note, getRealNoteDuration(track, note, tempo, data.getDuration(), measureIdx, bIndex), stroke);
                    int velocity = getRealVelocity(note, track, measureIdx, bIndex);
                    int channel = track.getChannel().getChannel();
                    int effectChannel = track.getChannel().getEffectChannel();
                    boolean percussionTrack = track.isPercussionTrack();
                    if (note.getEffect().isFadeIn()) {
                        channel = effectChannel;
                        makeFadeIn(sequence, trackId, start, duration, track.getChannel().getVolume(), channel);
                    }
                    if (note.getEffect().isGrace() && effectChannel >= 0 && !percussionTrack) {
                        channel = effectChannel;
                        int graceKey = track.getOffset() + note.getEffect().getGrace().getFret() + ((TGString) track.getStrings().get(note.getString() - 1)).getValue();
                        int graceLength = note.getEffect().getGrace().getDurationTime();
                        int graceVelocity = note.getEffect().getGrace().getDynamic();
                        long graceDuration = ((note.getEffect().getGrace().isDead()) ? applyStaticDuration(tempo, DEFAULT_DURATION_DEAD, graceLength) : graceLength);
                        if (note.getEffect().getGrace().isOnBeat() || (start - graceLength) < TGDuration.QUARTER_TIME) {
                            start += graceLength;
                            duration -= graceLength;
                        }
                        makeNote(sequence, trackId, graceKey, start - graceLength, graceDuration, graceVelocity, channel);
                    }
                    if (note.getEffect().isTrill() && effectChannel >= 0 && !percussionTrack) {
                        int trillKey = track.getOffset() + note.getEffect().getTrill().getFret() + ((TGString) track.getStrings().get(note.getString() - 1)).getValue();
                        long trillLength = note.getEffect().getTrill().getDuration().getTime();
                        boolean realKey = true;
                        long tick = start;
                        while (true) {
                            if (tick + 10 >= (start + duration)) {
                                break;
                            } else if ((tick + trillLength) >= (start + duration)) {
                                trillLength = (((start + duration) - tick) - 1);
                            }
                            makeNote(sequence, trackId, ((realKey) ? key : trillKey), tick, trillLength, velocity, channel);
                            realKey = (!realKey);
                            tick += trillLength;
                        }
                        continue;
                    }
                    if (note.getEffect().isTremoloPicking() && effectChannel >= 0) {
                        long tpLength = note.getEffect().getTremoloPicking().getDuration().getTime();
                        long tick = start;
                        while (true) {
                            if (tick + 10 >= (start + duration)) {
                                break;
                            } else if ((tick + tpLength) >= (start + duration)) {
                                tpLength = (((start + duration) - tick) - 1);
                            }
                            makeNote(sequence, trackId, key, tick, tpLength, velocity, channel);
                            tick += tpLength;
                        }
                        continue;
                    }
                    if (note.getEffect().isBend() && effectChannel >= 0 && !percussionTrack) {
                        channel = effectChannel;
                        makeBend(sequence, trackId, start, duration, note.getEffect().getBend(), channel);
                    } else if (note.getEffect().isTremoloBar() && effectChannel >= 0 && !percussionTrack) {
                        channel = effectChannel;
                        makeTremoloBar(sequence, trackId, start, duration, note.getEffect().getTremoloBar(), channel);
                    } else if (note.getEffect().isSlide() && effectChannel >= 0 && !percussionTrack) {
                        channel = effectChannel;
                        TGNote nextNote = getNextNote(note, track, measureIdx, bIndex, true);
                        makeSlide(sequence, trackId, note, nextNote, startMove, channel);
                    } else if (note.getEffect().isVibrato() && effectChannel >= 0 && !percussionTrack) {
                        channel = effectChannel;
                        makeVibrato(sequence, trackId, start, duration, channel);
                    }
                    if (note.getEffect().isHarmonic() && !percussionTrack) {
                        int orig = key;
                        if (note.getEffect().getHarmonic().isNatural()) {
                            for (int i = 0; i < TGEffectHarmonic.NATURAL_FREQUENCIES.length; i++) {
                                if ((note.getValue() % 12) == (TGEffectHarmonic.NATURAL_FREQUENCIES[i][0] % 12)) {
                                    key = ((orig + TGEffectHarmonic.NATURAL_FREQUENCIES[i][1]) - note.getValue());
                                    break;
                                }
                            }
                        } else {
                            if (note.getEffect().getHarmonic().isSemi() && !percussionTrack) {
                                makeNote(sequence, trackId, Math.min(127, orig), start, duration, Math.max(TGVelocities.MIN_VELOCITY, velocity - (TGVelocities.VELOCITY_INCREMENT * 3)), channel);
                            }
                            key = (orig + TGEffectHarmonic.NATURAL_FREQUENCIES[note.getEffect().getHarmonic().getData()][1]);
                        }
                        if ((key - 12) > 0) {
                            int hVelocity = Math.max(TGVelocities.MIN_VELOCITY, velocity - (TGVelocities.VELOCITY_INCREMENT * 4));
                            makeNote(sequence, trackId, (key - 12), start, duration, hVelocity, channel);
                        }
                    }
                    makeNote(sequence, trackId, Math.min(127, key), start, duration, velocity, channel);
                }
            }
        }
    }
