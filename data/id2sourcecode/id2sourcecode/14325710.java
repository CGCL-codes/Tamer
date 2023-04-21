    public int skipBytes(int count) throws IOException {
        int wished = buf.position() + count;
        buf.position(buf.position() + count);
        if (buf.position() < wished) {
            return (int) (wished - this.getChannel().size());
        } else {
            return count;
        }
    }
