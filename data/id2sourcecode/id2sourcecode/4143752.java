    @Override
    public int write(ByteBuffer source) throws IOException {
        if (this.xout != null) return this.xout.write(source); else if (this.fout != null) {
            int count = source.remaining();
            while (source.hasRemaining()) this.fout.getChannel().write(source);
            return count;
        } else return super.write(source);
    }
