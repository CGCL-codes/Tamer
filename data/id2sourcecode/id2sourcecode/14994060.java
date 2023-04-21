    private String computeChecksum(String csType) {
        logger.debug("checksumType is " + csType);
        String checksum = CHECKSUM_NONE;
        if (csType == null) {
            logger.warn("checksumType is null");
        }
        if (csType.equals(CHECKSUMTYPE_DISABLED)) {
            checksum = CHECKSUM_NONE;
            return checksum;
        }
        InputStream is = null;
        try {
            MessageDigest md = MessageDigest.getInstance(csType);
            logger.debug("Classname = " + this.getClass().getName());
            logger.debug("location = " + DSLocation);
            is = getContentStreamForChecksum();
            if (is != null) {
                byte buffer[] = new byte[5000];
                int numread;
                logger.debug("Reading content...");
                while ((numread = is.read(buffer, 0, 5000)) > 0) {
                    md.update(buffer, 0, numread);
                }
                is.close();
                logger.debug("...Done reading content");
                checksum = StringUtility.byteArraytoHexString(md.digest());
            }
        } catch (NoSuchAlgorithmException e) {
            checksum = CHECKSUM_NONE;
        } catch (StreamIOException e) {
            checksum = CHECKSUM_IOEXCEPTION;
            logger.warn("IOException reading datastream to generate checksum");
        } catch (IOException e) {
            checksum = CHECKSUM_IOEXCEPTION;
            logger.warn("IOException reading datastream to generate checksum");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.warn("IOException closing stream (computeChecksum) in finally");
                }
            }
        }
        return checksum;
    }
