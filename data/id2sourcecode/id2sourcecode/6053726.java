    public static void action(TRAC2001 trac) {
        Primitive active = trac.getActivePrimitive();
        boolean name = false;
        Channel ch = null;
        String mneumonic = active.getMneumonic();
        if (mneumonic.length() > 2) if (mneumonic.charAt(2) == 'n') name = true;
        byte channel = active.jGet();
        if (channel == (byte) '0') {
            if (name) {
                ch = trac.getChannel((byte) '1');
                if (ch != null) active.addValue(ch.getFileName());
            } else for (int i = 1; i <= TRACUtil.MAXCHANNELS; i++) {
                ch = trac.getChannel((byte) (48 + i));
                if (ch != null) trac.ioHandler.output(ch.toString());
            }
        } else {
            ch = trac.getChannel(channel);
            if (ch != null) {
                if (name) active.addValue(ch.getFileName()); else active.addValue(ch.getStatus());
            }
        }
    }
