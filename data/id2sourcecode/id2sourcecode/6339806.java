    public void noteReceived(ShortMessage inMessage, long inTimeStamp) {
        byte pitch = (byte) inMessage.getData1(), velocity = (byte) inMessage.getData2(), stringIndex = (byte) getString(inMessage.getChannel());
        if (stringIndex != -1) {
            byte fretIndex = (byte) getFret(pitch, stringIndex);
            if (fretIndex != -1) {
                switch(inMessage.getCommand()) {
                    case ShortMessage.NOTE_ON:
                        {
                            switch(f_Mode) {
                                case MiConfig.MODE_FRETBOARD_ECHO:
                                    if (velocity == 0 || velocity > f_MinVelocity) echo(stringIndex, fretIndex, velocity > 0);
                                    break;
                                case MiConfig.MODE_CHORDS_RECORDING:
                                    if (velocity == 0 || velocity > f_MinVelocity) echo(stringIndex, fretIndex, velocity > 0);
                                    chord_AddNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
                                    break;
                                case MiConfig.MODE_SCALES_RECOGNITION:
                                    if (velocity == 0 || velocity > f_MinVelocity) echo(stringIndex, fretIndex, velocity > 0);
                                    scale_AddNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
                                    break;
                                case MiConfig.MODE_SONG_RECORDING:
                                    if (velocity == 0 || velocity > f_MinVelocity) echo(stringIndex, fretIndex, velocity > 0);
                                    MiRecorder.instance().addNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
                                    break;
                            }
                        }
                        break;
                    case ShortMessage.NOTE_OFF:
                        switch(f_Mode) {
                            case MiConfig.MODE_FRETBOARD_ECHO:
                                echo(stringIndex, fretIndex, false);
                                break;
                            case MiConfig.MODE_CHORDS_RECORDING:
                                echo(stringIndex, fretIndex, false);
                                chord_AddNote(stringIndex, fretIndex, pitch, (byte) 0, inTimeStamp);
                                break;
                            case MiConfig.MODE_SCALES_RECOGNITION:
                                echo(stringIndex, fretIndex, false);
                                scale_AddNote(stringIndex, fretIndex, pitch, (byte) 0, inTimeStamp);
                                break;
                            case MiConfig.MODE_SONG_RECORDING:
                                echo(stringIndex, fretIndex, false);
                                MiRecorder.instance().addNote(stringIndex, fretIndex, pitch, (byte) 0, inTimeStamp);
                                break;
                        }
                        break;
                }
            }
        }
    }
