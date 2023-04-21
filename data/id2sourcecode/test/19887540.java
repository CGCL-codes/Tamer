    public byte[] digest() {
        byte[] b;
        b = digest.digest();
        if (DebugFile.trace) {
            DebugFile.writeln("digest: ");
        }
        updates = 0;
        return b;
    }
