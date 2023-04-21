    private void init() throws IOException {
        buf = this.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, this.getChannel().size());
        if (!buf.isLoaded()) buf.load();
    }
