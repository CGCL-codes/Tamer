    public long generateChecksum(String str) {
        byte[] bytes = str.getBytes();
        Checksum checksumEngine = new CRC32();
        checksumEngine.update(bytes, 0, bytes.length);
        long checksum = checksumEngine.getValue();
        return checksum;
    }
