    private void enqueueShortMessage(ShortMessage shortMessage, long lTime) {
        int nChannel = shortMessage.getChannel();
        switch(shortMessage.getCommand()) {
            case ShortMessage.NOTE_OFF:
                sendNoteOffEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
                break;
            case ShortMessage.NOTE_ON:
                sendNoteOnEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
                break;
            case ShortMessage.POLY_PRESSURE:
                sendKeyPressureEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
                break;
            case ShortMessage.CONTROL_CHANGE:
                sendControlChangeEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
                break;
            case ShortMessage.PROGRAM_CHANGE:
                sendProgramChangeEvent(lTime, nChannel, shortMessage.getData1());
                break;
            case ShortMessage.CHANNEL_PRESSURE:
                sendChannelPressureEvent(lTime, nChannel, shortMessage.getData1());
                break;
            case ShortMessage.PITCH_BEND:
                sendPitchBendEvent(lTime, nChannel, get14bitValue(shortMessage.getData1(), shortMessage.getData2()));
                break;
            case 0xF0:
                switch(shortMessage.getStatus()) {
                    case ShortMessage.MIDI_TIME_CODE:
                        sendMTCEvent(lTime, shortMessage.getData1());
                        break;
                    case ShortMessage.SONG_POSITION_POINTER:
                        sendSongPositionPointerEvent(lTime, get14bitValue(shortMessage.getData1(), shortMessage.getData2()));
                        break;
                    case ShortMessage.SONG_SELECT:
                        sendSongSelectEvent(lTime, shortMessage.getData1());
                        break;
                    case ShortMessage.TUNE_REQUEST:
                        sendTuneRequestEvent(lTime);
                        break;
                    case ShortMessage.TIMING_CLOCK:
                        sendMidiClockEvent(lTime);
                        break;
                    case ShortMessage.START:
                        sendStartEvent(lTime);
                        break;
                    case ShortMessage.CONTINUE:
                        sendContinueEvent(lTime);
                        break;
                    case ShortMessage.STOP:
                        sendStopEvent(lTime);
                        break;
                    case ShortMessage.ACTIVE_SENSING:
                        sendActiveSensingEvent(lTime);
                        break;
                    case ShortMessage.SYSTEM_RESET:
                        sendSystemResetEvent(lTime);
                        break;
                    default:
                        TDebug.out("AlsaMidiOut.enqueueShortMessage(): UNKNOWN EVENT TYPE: " + shortMessage.getStatus());
                }
                break;
            default:
                TDebug.out("AlsaMidiOut.enqueueShortMessage(): UNKNOWN EVENT TYPE: " + shortMessage.getStatus());
        }
    }
