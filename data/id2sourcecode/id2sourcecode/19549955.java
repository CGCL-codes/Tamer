    public void send(MidiMessage message, long timeStamp) {
        if (theApp != null) {
            try {
                ShortMessage msg = (ShortMessage) message;
                if (msg.getCommand() == ShortMessage.NOTE_ON) {
                    theApp.note(msg.getData1(), msg.getData2(), msg.getChannel(), 1);
                } else if (msg.getCommand() == ShortMessage.NOTE_OFF) {
                    theApp.note(msg.getData1(), msg.getData2(), msg.getChannel(), 0);
                } else if (msg.getCommand() == ShortMessage.CONTROL_CHANGE) {
                    theApp.cc(msg.getData1(), msg.getData2(), msg.getChannel());
                }
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                errorLog.addError(sw.toString());
            }
        }
    }
