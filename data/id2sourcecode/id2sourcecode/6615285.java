    public final long appendMessage(MboxFile source, long srcpos, int srcsize) throws IOException {
        long position = channel.size();
        if (position > 0) {
            channel.write(encoder.encode(CharBuffer.wrap("\n\n")), channel.size());
        }
        channel.write(encoder.encode(CharBuffer.wrap(DEFAULT_FROM__LINE)), channel.size());
        channel.write(source.getChannel().map(FileChannel.MapMode.READ_ONLY, srcpos, srcsize));
        return position;
    }
