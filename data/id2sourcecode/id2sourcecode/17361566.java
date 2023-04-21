    public SmafEvent[] getSmafEvents(MidiEvent midiEvent, SmafContext context) throws InvalidSmafDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int data1 = shortMessage.getData1();
        int data2 = shortMessage.getData2();
        switch(data1) {
            case 0:
                bankMSB[channel] = data2;
                break;
            case 32:
                bankLSB[channel] = data2;
                break;
            case 98:
                nrpnLSB[channel] = data2;
                break;
            case 99:
                nrpnMSB[channel] = data2;
                break;
            case 100:
                rpnLSB[channel] = data2;
                break;
            case 101:
                rpnMSB[channel] = data2;
                break;
            default:
                break;
        }
        return null;
    }
