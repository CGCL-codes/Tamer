    @Override
    public byte[] getChecksum() {
        if (checksum != null) return checksum;
        if (calculator == null) throw new IllegalStateException("MD5 calculator was not setup correctly and is null");
        return checksum = calculator.digest();
    }
