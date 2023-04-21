    public void write(final WriteAction action) {
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("config-", ".tmp", _configFile.getParentFile());
            final MessageDigest outputDigest = MessageDigest.getInstance("MD5");
            final OutputStream out = new DigestOutputStream(new FileOutputStream(tmpFile), outputDigest);
            try {
                action.writeConfiguration(out);
            } finally {
                out.close();
            }
            if (_inputDigest == null || !_configFile.exists() || !MessageDigest.isEqual(_inputDigest, outputDigest.digest())) {
                Logger.debug("non equal.. write file '%s'", _configFile);
                tmpFile.renameTo(_configFile);
            }
        } catch (final Exception e) {
            Logger.error("Could not write config. Error occured: ", e);
        } finally {
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }
    }
