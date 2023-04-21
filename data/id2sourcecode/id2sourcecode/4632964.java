    public String getDigest(org.osid.shared.Type algorithmType) throws org.osid.filing.FilingException {
        logger.logMethod(algorithmType);
        if (algorithmType == null) {
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.NULL_ARGUMENT);
        }
        if (!algorithmType.isEqual(FilingType.MD5_DIGEST.getType())) {
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.UNKNOWN_TYPE);
        }
        String hash;
        try {
            java.io.InputStream is = new java.io.FileInputStream(this.file);
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] b = new byte[10240];
            int count;
            while ((count = is.read(b)) > -1) {
                md.update(b, 0, count);
            }
            hash = new String(md.digest());
        } catch (Exception e) {
            logger.logError("unable to get md5 hash", e);
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.IO_ERROR);
        }
        logger.logTrace("md5 hash of " + this.file.getPath() + " is " + hash);
        return (hash);
    }
