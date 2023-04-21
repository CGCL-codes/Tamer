    public static String MidiMessageToString(MidiMessage msg, long time) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg.getClass().getName() + " st=" + msg.getStatus());
        if (msg instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) msg;
            sb.append(" ch=" + sm.getChannel());
            String cmd;
            switch(sm.getCommand()) {
                case 128:
                    cmd = new String("NOTE_OFF");
                    break;
                case 144:
                    cmd = new String("NOTE_ON");
                    break;
                case 176:
                    cmd = new String("CTRL_CHG");
                    break;
                case 192:
                    cmd = new String("PRG_CHG");
                    break;
                case 208:
                    cmd = new String("AFTRTCH");
                    break;
                case 224:
                    cmd = new String("PTCH_BND");
                    break;
                default:
                    cmd = new String("" + sm.getCommand());
            }
            sb.append(" cmd=" + cmd);
            sb.append(" d1=" + sm.getData1());
            sb.append(" d2=" + sm.getData2());
        } else if (msg instanceof SysexMessage) {
            SysexMessage sm = (SysexMessage) msg;
            sb.append(" SysEx");
        } else if (msg instanceof MetaMessage) {
            MetaMessage mm = (MetaMessage) msg;
            sb.append("ty=" + mm.getType());
        }
        sb.append(" t=" + time);
        return sb.toString();
    }
