    public void recallSequence(Sequence snapshot) {
        Track[] tracks = snapshot.getTracks();
        Track track;
        SynthControls synthControls;
        int instanceIndex = -1;
        for (int t = 0; t < tracks.length; t++) {
            track = tracks[t];
            MidiMessage msg = track.get(0).getMessage();
            if (isMeta(msg) && getType(msg) == TRACK_NAME) {
            } else continue;
            synthControls = rackControls.getSynthControls(t);
            if (synthControls == null) {
                System.err.println("Synth recall: failed to get synth controls " + t);
                continue;
            }
            int providerId = -1;
            int moduleId = -1;
            NativeSupport ns = synthControls.getNativeSupport();
            if (ns != null && ns.canPersistMidi()) {
                ns.recall(track, 2);
            } else if (synthControls instanceof ChannelledSynthControls) {
                SynthChannelControls channelControls = null;
                for (int i = 2; i < track.size(); i++) {
                    msg = track.get(i).getMessage();
                    if (isNote(msg)) {
                        int controller = getData1(msg);
                        int cid = getData2(msg);
                        if (channelControls != null) {
                            channelControls.setMappedControlId(controller, cid);
                        } else {
                            System.err.println("Synth recall: no channelControl to map controller " + controller + " to " + cid);
                        }
                    }
                    if (!isControl(msg)) continue;
                    if (instanceIndex != getInstanceIndex(msg)) {
                        instanceIndex = getInstanceIndex(msg);
                        int chan = instanceIndex - 1;
                        channelControls = ((ChannelledSynthControls) synthControls).getChannelControls(chan);
                        if (channelControls == null) {
                            System.err.println("Synth recall: failed to get channel controls " + t + "/" + chan);
                            break;
                        }
                        providerId = channelControls.getProviderId();
                        moduleId = channelControls.getId();
                    }
                    if (getProviderId(msg) != providerId || getModuleId(msg) != moduleId) continue;
                    int cid = getControlId(msg);
                    Control control = channelControls.deepFind(cid);
                    if (control == null) {
                        continue;
                    }
                    int newValue = getValue(msg);
                    if (newValue == control.getIntValue()) continue;
                    control.setIntValue(newValue);
                }
            } else {
                providerId = synthControls.getProviderId();
                moduleId = synthControls.getId();
                for (int i = 2; i < track.size(); i++) {
                    msg = track.get(i).getMessage();
                    if (!isControl(msg)) continue;
                    if (getProviderId(msg) != providerId || getModuleId(msg) != moduleId) continue;
                    int cid = getControlId(msg);
                    Control control = synthControls.deepFind(cid);
                    if (control == null) {
                        continue;
                    }
                    int newValue = getValue(msg);
                    if (newValue == control.getIntValue()) continue;
                    control.setIntValue(newValue);
                }
            }
        }
    }
