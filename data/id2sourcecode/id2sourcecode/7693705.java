    private boolean haveFile(File file, boolean getSHA1) throws IOException, DatabaseException {
        if (!file.exists()) {
            return false;
        }
        protocol.write(protocol.new FileInfoReq(file.getName(), getSHA1), channel);
        FileInfoResp statResp = protocol.read(channel, Protocol.FileInfoResp.class);
        long fileLength = file.length();
        if (statResp.getFileLength() != fileLength) {
            return false;
        }
        if (statResp.getDigestSHA1().length == 0) {
            assert (!getSHA1);
            return haveFile(file, true);
        }
        byte digest[] = LogFileFeeder.getSHA1Digest(file, file.length()).digest();
        return Arrays.equals(digest, statResp.getDigestSHA1());
    }
