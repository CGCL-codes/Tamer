    @Override
    public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException {
        Class<?> clazz = example.getClass();
        try {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            Map<String, Property> properties = new HashMap<String, Property>();
            for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
                String name = descriptor.getName();
                if ("class".equals(name)) {
                    continue;
                }
                if (!isAllowedByIncludeExcludeRules(name)) {
                    continue;
                }
                if (readRequired && descriptor.getReadMethod() == null) {
                    continue;
                }
                if (writeRequired && descriptor.getWriteMethod() == null) {
                    continue;
                }
                properties.put(name, new EclipseLinkPropertyDescriptorProperty(descriptor));
            }
            return properties;
        } catch (Exception ex) {
            throw new MarshallException(clazz, ex);
        }
    }
