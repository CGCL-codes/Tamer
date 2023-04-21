    void readWriteLockProperties() {
        final Properties properties = new Properties();
        if (fileSystem.readPropertiesFile(writeLockPropertiesFile, properties)) {
            writeLockDelay = getLongProperty(properties, "delay-milliseconds", -1L);
            writeLockTargetLimit = (int) getLongProperty(properties, "target-limit", 0L);
        }
        log.debug("Read write lock properties " + properties);
    }
