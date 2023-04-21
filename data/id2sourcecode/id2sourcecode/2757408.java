    public final void readFromStream(DataInputStream stream) throws IOException {
        int cnt = stream.readInt();
        baos.reset();
        for (int i = 0; i < cnt; i++) {
            baos.write(stream.readByte());
        }
    }
