    public static void saveConfig(final MetaInfo metaInfo, final OutputStream output) {
        Config config = metaInfo.getConfig();
        if (output instanceof DigestOutputStream) {
            MessageDigest digest = DigestOutputStream.class.cast(output).getMessageDigest();
            config.write("encrypted", true);
            byte[] hash = digest.digest();
            config.write("Cipher.MessageDigest.value", toString(hash));
            try {
                VersionControl.deleteAllVersions(metaInfo, true);
            } catch (VersionException exception) {
                MLogger.exception(exception);
            }
        } else {
            config.removeBoolean("encrypted");
            config.removeString("Cipher.MessageDigest.value");
            config.removeString("Cipher.salt");
            config.removeString("Cipher.transformation");
        }
        config.save();
    }
