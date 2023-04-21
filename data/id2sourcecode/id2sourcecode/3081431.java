    protected void readAndVerifyVersion() throws IOException {
        version = in.readInt();
        writerVersion = new Version(in.readInt());
        releaseVersion = new Version(in.readInt());
        if (version != VERSION2) {
            if (releaseVersion.isExperimental()) {
                throw new IOException(String.format("cannot read unreleased workspace version %d written by experimental R %s", version, writerVersion));
            } else {
                throw new IOException(String.format("cannot read workspace version %d written by R %s; need R %s or newer", version, releaseVersion, releaseVersion));
            }
        }
    }
