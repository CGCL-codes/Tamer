    protected static java.util.Map getStaticProperties() {
        if (properties.isEmpty()) {
            properties.put(new CapabilityType("MIT", "remoteAccess"), Boolean.TRUE);
            properties.put(new CapabilityType("MIT", "localAccess"), Boolean.TRUE);
            properties.put(new CapabilityType("MIT", "metaDataCache"), Boolean.TRUE);
            properties.put(new CapabilityType("MIT", "replication"), Boolean.FALSE);
            properties.put(new CapabilityType("MIT", "quota"), Boolean.FALSE);
            properties.put(new CapabilityType("MIT", "readwrite"), Boolean.TRUE);
        }
        return java.util.Collections.unmodifiableMap(properties);
    }
