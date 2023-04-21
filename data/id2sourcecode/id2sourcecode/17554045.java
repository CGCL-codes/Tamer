    public static void action(TRAC2001 trac) {
        Channel ch = trac.getChannel(trac.getActivePrimitive().jGet());
        if (ch != null) {
            try {
                ch.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
