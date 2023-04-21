    public String digestMD5(final InputStream data) throws DigestCreationException {
        MessageDigest md;
        String result;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[128];
            int read = -1;
            while ((read = data.read(buf)) > 0) {
                md.update(buf, 0, read);
            }
            result = convertToHex(md.digest());
        } catch (Exception e) {
            result = null;
            throw new DigestCreationException("Could not read in datastream to generate digest", e);
        } finally {
            try {
                data.close();
            } catch (final IOException e) {
                Activator.log(Status.WARNING, "Could not close data stream");
            }
        }
        return result;
    }
