    public static void action(TRAC2001 trac) {
        Primitive active = trac.getActivePrimitive();
        if (active.length() >= 1) {
            Channel ch = trac.getChannel(active.jGet());
            if (ch != null) {
                try {
                    active.addValue(ch.getFilePointer());
                } catch (Exception e) {
                    trac.zReturn(active.getArg(1));
                }
            }
        }
    }
