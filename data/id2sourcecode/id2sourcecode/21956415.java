    private String computeChecksum(String csType) {
        LOG.debug("checksumType is " + csType);
        String checksum = "none";
        if (csType == null) LOG.warn("checksumType is null");
        if (csType.equals(CHECKSUMTYPE_DISABLED)) {
            checksum = "none";
            return (checksum);
        }
        try {
            MessageDigest md = MessageDigest.getInstance(csType);
            LOG.debug("Classname = " + this.getClass().getName());
            LOG.debug("location = " + this.DSLocation);
            InputStream is = getContentStreamForChecksum();
            if (is != null) {
                byte buffer[] = new byte[5000];
                int numread;
                LOG.debug("Reading content...");
                while ((numread = is.read(buffer, 0, 5000)) > 0) {
                    md.update(buffer, 0, numread);
                }
                LOG.debug("...Done reading content");
                checksum = StringUtility.byteArraytoHexString(md.digest());
            }
        } catch (NoSuchAlgorithmException e) {
            checksum = "none";
        } catch (StreamIOException e) {
            checksum = CHECKSUM_IOEXCEPTION;
            LOG.warn("IOException reading datastream to generate checksum");
        } catch (IOException e) {
            checksum = CHECKSUM_IOEXCEPTION;
            LOG.warn("IOException reading datastream to generate checksum");
        }
        return (checksum);
    }
