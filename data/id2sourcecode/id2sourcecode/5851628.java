    public PropertyAccessStrategy createPropertyAccessStrategy(ClassMappingInformation classMapping, String propertyName) {
        if (classMapping.usesFieldAccess()) {
            Field field = getField(classMapping.getEntityType(), propertyName);
            return new ReflectionFieldAccessStrategy(field, beanInitializer);
        } else {
            Method readMethod = getReadMethod(classMapping.getEntityType(), propertyName);
            Method writeMethod = getWriteMethod(classMapping.getEntityType(), propertyName);
            return new ReflectionMethodAccessStrategy(readMethod, writeMethod);
        }
    }
