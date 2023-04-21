    public static GatheringByteChannel getChannel(OutputStream out) {
        if (out instanceof FileOutputStream) {
            return ((FileOutputStream) out).getChannel();
        } else {
            return new OutputStreamChannel(out);
        }
    }
