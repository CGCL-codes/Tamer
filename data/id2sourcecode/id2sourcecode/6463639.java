    private void processShortMessage(ShortMessage msg) {
        switch(msg.getCommand()) {
            case 0x80:
                {
                    processNoteOffEvent(msg.getData1(), msg.getChannel());
                    break;
                }
            case 0x90:
                {
                    if (msg.getData2() == 0) {
                        processNoteOffEvent(msg.getData1(), msg.getChannel());
                    } else {
                        processNoteOnEvent(msg.getData1(), msg.getData2(), msg.getChannel());
                    }
                    break;
                }
            case 0xa0:
                {
                    break;
                }
            case 0xb0:
                {
                    processControlChange(msg.getData1(), msg.getData2(), msg.getChannel());
                    break;
                }
            case 0xc0:
                {
                    currentProgram = msg.getData1();
                    break;
                }
            case 0xd0:
                {
                    break;
                }
            case 0xe0:
                {
                    break;
                }
            case 0xF0:
                {
                    break;
                }
        }
    }
