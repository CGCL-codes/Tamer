    public static void action(TRAC2001 trac) {
        Primitive active = trac.getActivePrimitive();
        if (active.length() >= 3) {
            Channel ch = trac.getChannel(active.jGet());
            if (ch != null) {
                try {
                    active.addValue(ch.read(active.getArg(1), active.getArg(2)));
                } catch (Exception e) {
                    trac.zReturn(active.getArg(3));
                }
            }
        }
    }
