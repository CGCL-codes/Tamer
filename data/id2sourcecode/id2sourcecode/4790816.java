    private static List getPatchs(List events) {
        Patch[] channels = new Patch[16];
        Iterator it = events.iterator();
        while (it.hasNext()) {
            MidiEvent event = (MidiEvent) it.next();
            MidiMessage msg = event.getMessage();
            if (msg instanceof ShortMessage) {
                ShortMessage shortMessage = (ShortMessage) msg;
                int channel = shortMessage.getChannel();
                if (channel >= 0 && channel < channels.length) {
                    int command = shortMessage.getCommand();
                    int data1 = shortMessage.getData1();
                    int data2 = shortMessage.getData2();
                    int bank = (command == ShortMessage.CONTROL_CHANGE && data1 == MidiControllers.BANK_SELECT ? data2 : -1);
                    int program = (command == ShortMessage.PROGRAM_CHANGE ? data1 : -1);
                    if (bank >= 0 || program >= 0) {
                        if (bank < 0) {
                            bank = (channels[channel] != null ? channels[channel].getBank() : 0);
                        }
                        if (program < 0) {
                            program = (channels[channel] != null ? channels[channel].getProgram() : 0);
                        }
                        channels[channel] = new Patch(bank, program);
                    }
                }
            }
        }
        List patchs = new ArrayList();
        for (int i = 0; i < channels.length; i++) {
            if (channels[i] != null) {
                boolean patchExists = false;
                Iterator patchIt = patchs.iterator();
                while (patchIt.hasNext()) {
                    Patch patch = (Patch) patchIt.next();
                    if (patch.getBank() == channels[i].getBank() && patch.getProgram() == channels[i].getProgram()) {
                        patchExists = true;
                    }
                }
                if (!patchExists) {
                    patchs.add(channels[i]);
                }
            }
        }
        patchs.add(new Patch(128, 0));
        return patchs;
    }
