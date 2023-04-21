    private static Long calcHash(String patchText) {
        byte[] bytes = patchText.getBytes();
        Checksum checksumEngine = new CRC32();
        checksumEngine.update(bytes, 0, bytes.length);
        return checksumEngine.getValue();
    }
