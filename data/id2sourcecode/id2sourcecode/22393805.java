    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sMsg = (ShortMessage) message;
            if (out != null) out.println("Type: Short Message; Command : " + sMsg.getCommand() + "; Channel: " + sMsg.getChannel() + "; data1: " + sMsg.getData1() + "; data2: " + sMsg.getData2());
            if ((sMsg.getCommand() == ShortMessage.NOTE_ON) || (sMsg.getCommand() == ShortMessage.NOTE_OFF)) {
                try {
                    sMsg.setMessage(sMsg.getCommand(), sMsg.getChannel(), sMsg.getData1() + offset, sMsg.getData2());
                } catch (InvalidMidiDataException mde) {
                    mde.printStackTrace();
                }
            }
            destination.send(sMsg, timeStamp);
        } else {
            if (out != null) out.println("Non ShortMessage: " + message);
            destination.send(message, timeStamp);
        }
    }
