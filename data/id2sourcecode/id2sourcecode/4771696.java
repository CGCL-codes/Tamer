    public HL7Message read() throws HL7IOException {
        throw new HL7IOException("read:", new UnsupportedOperationException("writer does not read."));
    }
