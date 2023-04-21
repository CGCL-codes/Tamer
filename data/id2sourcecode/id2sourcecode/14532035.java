    public byte[] digest() {
        byte[] b;
        b = digest.digest();
        if (log.level > 3) {
            log.println("digest: ");
            Hexdump.hexdump(log, b, 0, b.length);
            log.flush();
        }
        updates = 0;
        return b;
    }
