    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(overflowSize);
        s.writeInt(this.bytesBuffered);
        InputStream in = null;
        if (isOverflowToDisk()) {
            close();
            in = new FileInputStream(this.tempFile);
        } else {
            in = new ByteArrayInputStream(getByteArray());
        }
        for (int i = 0; i < this.bytesBuffered; i++) {
            s.write(in.read());
        }
        in.close();
    }
