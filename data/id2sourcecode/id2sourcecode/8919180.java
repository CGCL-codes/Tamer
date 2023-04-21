    public void close() throws IOException {
        if (this.in != null) {
            skip();
            this.in = null;
            if (this.digest != null) {
                this.digestStr = Base32.encode(this.digest.digest());
            }
        }
    }
