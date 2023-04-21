    public void send(MidiMessage msg, long time) {
        System.out.println("-------------------");
        System.out.println("Length: " + msg.getLength());
        System.out.println("Status: " + msg.getStatus());
        System.out.println("Time: " + (int) time / 1000);
        System.out.println("-------------------");
        this.receiver.send(msg, time);
        if (msg instanceof ShortMessage) {
            ShortMessage msg2 = (ShortMessage) msg;
            System.out.println("Channel: " + msg2.getChannel());
            System.out.println("Command: " + msg2.getCommand());
            System.out.println("Data1: " + msg2.getData1());
            System.out.println("Data2: " + msg2.getData2());
            System.out.println("Length: " + msg2.getLength());
            System.out.println("Status: " + msg2.getStatus());
            if (msg2.getStatus() == ShortMessage.NOTE_ON) {
                System.out.print("NOTE ON");
            }
        }
    }
