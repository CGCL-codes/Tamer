    public static long hashValue(ShortMessage mess) {
        long chn = mess.getChannel();
        long cmd = mess.getCommand();
        long cntrl;
        if (cmd == ShortMessage.CONTROL_CHANGE) cntrl = mess.getData1(); else if (cmd == ShortMessage.PITCH_BEND) cntrl = 0; else {
            System.out.println(" Don't know what to do with " + mess);
            return -1;
        }
        return ((chn << 8 + cmd) << 8) + cntrl;
    }
