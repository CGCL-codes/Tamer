    public static String messageToString(MidiMessage message) {
        String strMessage = null;
        if (message instanceof ShortMessage) {
            ShortMessage smessage = (ShortMessage) message;
            switch(smessage.getCommand()) {
                case 0x80:
                    strMessage = "note Off " + getKeyName(smessage.getData1()) + " velocit� : " + smessage.getData2();
                    break;
                case 0x90:
                    strMessage = "note On " + getKeyName(smessage.getData1()) + " velocit� : " + smessage.getData2();
                    break;
            }
            if (smessage.getCommand() != 0xF0) {
                int nChannel = smessage.getChannel() + 1;
                String strChannel = "channel " + nChannel + ": ";
                strMessage = strChannel + strMessage;
            }
            return strMessage;
        } else return null;
    }
