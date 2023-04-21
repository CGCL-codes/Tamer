    private boolean addAnnotatedProperties(Class<?> clazz, PropertyDescriptor[] descriptors, List<SerializableProperty> properties) throws JsonException {
        Map<String, JsonProperty> jsonProperties = gatherJsonPropertyNames(clazz);
        for (PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            if (name.equals("class")) continue;
            if (name.equals("hibernateLazyInitializer")) continue;
            JsonProperty anno = jsonProperties.get(name);
            if (anno == null || (!anno.read() && !anno.write())) continue;
            Method readMethod = descriptor.getReadMethod();
            if (!anno.read() || (readMethod != null && readMethod.getDeclaringClass() != clazz)) readMethod = null;
            Method writeMethod = descriptor.getWriteMethod();
            if (!anno.write() || (writeMethod != null && writeMethod.getDeclaringClass() != clazz)) writeMethod = null;
            if (readMethod == null && writeMethod == null) continue;
            SerializableProperty prop = new SerializableProperty();
            prop.setName(name);
            prop.setReadMethod(readMethod);
            prop.setWriteMethod(writeMethod);
            if (!StringUtils.isEmpty(anno.alias())) prop.setAlias(anno.alias());
            prop.setSuppressDefaultValue(anno.suppressDefaultValue());
            prop.setIncludeHints(anno.includeHints());
            maybeAddProperty(properties, prop);
        }
        return !jsonProperties.isEmpty();
    }
