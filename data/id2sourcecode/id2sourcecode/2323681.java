    public static void action(TRAC2001 trac) {
        Primitive active = trac.getActivePrimitive();
        if (active.length() >= 2) {
            Channel ch = trac.getChannel(active.jGet());
            if (ch != null) {
                try {
                    ch.generate(active.getArg(1));
                } catch (Exception e) {
                    trac.zReturn(active.getArg(2));
                }
            }
        }
    }
