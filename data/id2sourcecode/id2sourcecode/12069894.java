    public static Writer interruptibleWriter(FileOutputStream f_out) {
        return Channels.newWriter(f_out.getChannel(), Charset.defaultCharset().name());
    }
