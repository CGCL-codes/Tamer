    private boolean fileCheckSumChanged(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance(Constants.DIGEST_ALGORITHM);
        byte[] bytes = FSUtils.readFile(file);
        md.update(bytes);
        String checksum = FormatUtils.formatBytesAsHexString(md.digest());
        return !checksum.equals(filesChecksums.get(file.getName()));
    }
