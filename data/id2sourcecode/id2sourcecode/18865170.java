    public void dumpHttpHeader(final PrintStream stream) throws IOException {
        if (this.contentHeaderStream == null) {
            return;
        }
        for (int available = this.contentHeaderStream.available(); this.contentHeaderStream != null && (available = this.contentHeaderStream.available()) > 0; ) {
            byte[] buffer = new byte[available];
            int read = read(buffer, 0, available);
            stream.write(buffer, 0, read);
        }
    }
