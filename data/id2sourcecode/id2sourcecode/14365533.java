    @Override
    public void afterPropertiesSet() {
        Assert.notNull(schema, "'schema' not set");
        Assert.notNull(authoritiesRegistry, "'authoritiesRegistry' not set");
        descriptors.clear();
        for (Map.Entry<String, Properties> entry : schema.entrySet()) {
            String propName = entry.getKey();
            Properties propProperties = entry.getValue();
            String readableName = propProperties.getProperty("name", propName);
            String description = propProperties.getProperty("description", "");
            AccessRule readRule = buildAccessRule(propProperties, "read", "access");
            AccessRule writeRule = buildAccessRule(propProperties, "write", "access");
            PropertyDescriptor desc = new PropertyDescriptor(propName, readableName, description, readRule, writeRule);
            descriptors.put(propName, desc);
        }
        Assert.notEmpty(descriptors, "'schema' map is empty");
    }
