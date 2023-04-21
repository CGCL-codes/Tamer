    public static Profile create(File profileDirectory) throws IOException {
        profileDirectory.mkdir();
        final RandomAccessFile propertiesFileAccess = openPropertiesFile(profileDirectory);
        boolean success = false;
        try {
            FileLock lock = propertiesFileAccess.getChannel().tryLock();
            if (lock == null) throw new IOException("Unable to obtain log in newly created profile");
            final Profile profile = new Profile(null, profileDirectory, propertiesFileAccess);
            profile.saveModelSnapshot(new ModelSnapshot());
            success = true;
            return profile;
        } finally {
            if (!success) propertiesFileAccess.close();
        }
    }
