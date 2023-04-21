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
            org.osid.shared.ByteValueIterator bvi = read(0);
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] b = new byte[1];
            while (bvi.hasNextByteValue()) {
                b[0] = bvi.nextByteValue();
                md.update(b, 0, 1);
            }
            hash = new String(md.digest());
        } catch (Exception e) {
            logger.logError("unable to get md5 hash", e);
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.IO_ERROR);
        } catch (org.osid.shared.SharedException se) {
            logger.logError("unable to get data", se);
            throw new org.osid.filing.FilingException(org.osid.filing.FilingException.IO_ERROR);
        }
        logger.logTrace("md5 hash is " + hash);
        return (hash);
    }
