    @Deprecated
    protected void setUpKeys() {
        getPlayOptions();
        if (!isDrumLane()) {
            getPlayOptions().drumMapped = false;
        } else {
            if (playOptions.noteMap != null) {
                playOptions.drumMapped = true;
            }
        }
        notifyFocusListeners();
        if (true) {
            return;
        }
        keyNames = null;
        if (!isDrumLane()) {
            playOptions.drumMapped = false;
        } else {
            if (playOptions.noteMap != null) {
                playOptions.drumMapped = true;
            }
            Instrument inst = null;
            MidiDevice dev = getMidiDevice();
            if (dev instanceof SynthRack) {
                Synth syn = ((SynthRack) dev).getSynth(midiChannel);
                if (syn instanceof MySampler) {
                    keyNames = new String[128];
                    MySampler mys = (MySampler) syn;
                    SampledSoundSettings[][] ssss = mys.sampledSounds;
                    for (int i = 0; i < 128; i++) {
                        if (ssss[i][0] != null) {
                            keyNames[i] = ssss[i][0].toString();
                        }
                    }
                }
            } else if (dev instanceof SynthWrapper) {
                dev = ((SynthWrapper) dev).getRealDevice();
                if (dev instanceof Synthesizer) {
                    Synthesizer synth = (Synthesizer) dev;
                    MyPatch patch = getProgram();
                    Method getChannels = null;
                    Instrument insts[] = synth.getLoadedInstruments();
                    for (Instrument ins : insts) {
                        Instrument li = (Instrument) ins;
                        boolean[] channels = null;
                        try {
                            if (getChannels != null) {
                                if (getChannels.getDeclaringClass() != li.getClass()) {
                                    getChannels = null;
                                }
                            }
                            if (getChannels == null) {
                                getChannels = li.getClass().getMethod("getChannels");
                            }
                            if (getChannels != null) {
                                channels = (boolean[]) getChannels.invoke(li, (Object[]) null);
                            }
                        } catch (Exception e) {
                        }
                        if (channels != null) {
                            if ((ins.getPatch().getProgram() == patch.prog) && channels[midiChannel]) {
                                inst = ins;
                                break;
                            }
                        }
                    }
                    if (inst == null) {
                        insts = synth.getAvailableInstruments();
                        for (Instrument ins : insts) {
                            Instrument li = (Instrument) ins;
                            boolean[] channels = null;
                            try {
                                if (getChannels != null) {
                                    if (getChannels.getDeclaringClass() != li.getClass()) {
                                        getChannels = null;
                                    }
                                }
                                if (getChannels == null) {
                                    getChannels = li.getClass().getMethod("getChannels");
                                }
                                if (getChannels != null) {
                                    channels = (boolean[]) getChannels.invoke(li, (Object[]) null);
                                }
                            } catch (Exception e) {
                            }
                            if (channels != null) {
                                if ((ins.getPatch().getProgram() == patch.prog) && channels[midiChannel]) {
                                    inst = ins;
                                    break;
                                }
                            }
                        }
                    }
                    if (inst != null) {
                        try {
                            Method getKeys = inst.getClass().getMethod("getKeys");
                            if (getKeys != null) {
                                keyNames = (String[]) getKeys.invoke(inst, (Object[]) null);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        notifyFocusListeners();
    }
