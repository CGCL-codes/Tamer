    protected void receive(MidiMessage message, long lTimeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage shortMsg = (ShortMessage) message;
            int nChannel = shortMsg.getChannel();
            int nCommand = shortMsg.getCommand();
            int nData1 = shortMsg.getData1();
            int nData2 = shortMsg.getData2();
            switch(nCommand) {
                case ShortMessage.NOTE_OFF:
                    getChannel(nChannel).noteOff(nData1, nData2);
                    break;
                case ShortMessage.NOTE_ON:
                    getChannel(nChannel).noteOn(nData1, nData2);
                    break;
                case ShortMessage.POLY_PRESSURE:
                    getChannel(nChannel).setPolyPressure(nData1, nData2);
                    break;
                case ShortMessage.CONTROL_CHANGE:
                    getChannel(nChannel).controlChange(nData1, nData2);
                    break;
                case ShortMessage.PROGRAM_CHANGE:
                    getChannel(nChannel).programChange(nData1);
                    break;
                case ShortMessage.CHANNEL_PRESSURE:
                    getChannel(nChannel).setChannelPressure(nData1);
                    break;
                case ShortMessage.PITCH_BEND:
                    getChannel(nChannel).setPitchBend(nData1 | (nData2 << 7));
                    break;
                default:
            }
        }
    }
