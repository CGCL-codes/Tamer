    public static Profile load(final File profileDirectory) throws CorruptProfileException, IOException, ProfileInUseException {
        if (!profileDirectory.exists()) {
            return null;
        }
        if (!profileDirectory.isDirectory()) throw new CorruptProfileException();
        final RandomAccessFile propertiesFileAccess = openPropertiesFile(profileDirectory);
        boolean success = false;
        try {
            FileLock lock = propertiesFileAccess.getChannel().tryLock();
            if (lock == null) throw new ProfileInUseException(profileDirectory);
            Properties properties = new Properties();
            try {
                properties.load(Channels.newInputStream(propertiesFileAccess.getChannel()));
            } catch (FileNotFoundException e) {
                throw new CorruptProfileException();
            }
            if (!ApplicationConstants.NAME.equals(properties.getProperty(APPLICATION_NAME_PROPERTY))) {
                throw new CorruptProfileException();
            }
            final Version version = readRequiredVersionProperty(properties, VERSION_PROPERTY);
            if (version.getBuild() != Build.RELEASE) throw new CorruptProfileException();
            final Profile profile = new Profile(version, profileDirectory, propertiesFileAccess);
            if (version.compareTo(VERSION) < 0) profile.saveModelSnapshot(profile.loadModelSnapshot());
            final File uploadsDirectory = new File(profileDirectory, UPLOADS_DIRECTORY_NAME);
            if (!uploadsDirectory.isDirectory()) throw new CorruptProfileException();
            success = true;
            return profile;
        } finally {
            if (!success) propertiesFileAccess.close();
        }
    }
