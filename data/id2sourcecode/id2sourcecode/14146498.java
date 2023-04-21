    public static void action(TRAC2001 trac) {
        Primitive active = trac.getActivePrimitive();
        if (active.length() >= 1) {
            Channel ch = trac.getChannel(active.jGet());
            if (ch != null) {
                try {
                    ch.write(active.getArg(1));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
