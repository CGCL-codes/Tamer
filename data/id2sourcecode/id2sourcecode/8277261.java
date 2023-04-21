    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.overflowSize = s.readInt();
        int bufferSize = s.readInt();
        for (int i = 0; i < bufferSize; i++) {
            write(s.read());
        }
    }
