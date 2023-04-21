    private void rememberFileChecksum(Attachment att) throws Exception {
        MessageDigest md = MessageDigest.getInstance(Constants.DIGEST_ALGORITHM);
        md.update(att.getData());
        String checksum = FormatUtils.formatBytesAsHexString(md.digest());
        filesChecksums.put(att.getName(), checksum);
    }
