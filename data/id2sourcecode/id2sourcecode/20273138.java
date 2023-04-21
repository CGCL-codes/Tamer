    public void flush() throws IOException {
        if (this.outStream != null) {
            this.outStream.flush();
        } else if (this.stream != null) {
            this.stream.getChannel().force(false);
        }
    }
