    public void transport(MidiMessage msg, long timestamp) {
        if (isChannel(msg)) {
            int chan = ChannelMsg.getChannel(msg);
            SynthChannel synthChannel = synthChannels[chan];
            if (synthChannel == null) return;
            if (NoteMsg.isNote(msg)) {
                int pitch = NoteMsg.getPitch(msg);
                int velocity = NoteMsg.getVelocity(msg);
                boolean on = NoteMsg.isOn(msg);
                if (on) {
                    synthChannel.noteOn(pitch, velocity);
                } else {
                    synthChannel.noteOff(pitch, velocity);
                }
            } else {
                int cmd = getCommand(msg);
                switch(cmd) {
                    case PITCH_BEND:
                        synthChannel.setPitchBend(getData1and2(msg));
                        break;
                    case CONTROL_CHANGE:
                        int controller = getData1(msg);
                        if (controller == ALL_CONTROLLERS_OFF) {
                            synthChannel.resetAllControllers();
                        } else if (controller == ALL_NOTES_OFF) {
                            synthChannel.allNotesOff();
                        } else if (controller == ALL_SOUND_OFF) {
                            synthChannel.allSoundOff();
                        } else {
                            synthChannel.controlChange(controller, getData2(msg));
                        }
                        break;
                    case ChannelMsg.CHANNEL_PRESSURE:
                        synthChannel.setChannelPressure(getData1(msg));
                        break;
                }
            }
        }
    }
