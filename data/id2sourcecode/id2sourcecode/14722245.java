    public ChecksummerImpl getChecksummer(TransferredFile file, long len) {
        return new ChecksummerImpl(file.getChannel(), len);
    }
