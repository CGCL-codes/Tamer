    public PropertyAccessStrategy createPropertyAccessStrategy(ClassMappingInformation classMapping, String propertyName) {
        Class<?> entityType = classMapping.getEntityType();
        String className = entityType.getName();
        boolean usesFieldAccess = classMapping.usesFieldAccess();
        Member readMember;
        Member writeMember;
        if (usesFieldAccess) {
            readMember = getField(entityType, propertyName);
            writeMember = readMember;
        } else {
            readMember = getReadMethod(entityType, propertyName);
            writeMember = getWriteMethod(entityType, propertyName);
        }
        if (!isAccessible(readMember) || !isAccessible(writeMember)) {
            return super.createPropertyAccessStrategy(classMapping, propertyName);
        }
        PropertyAccessStrategy propertyAccessStrategy = findPropertyAccessStrategy(className, propertyName);
        if (propertyAccessStrategy != null) {
            return propertyAccessStrategy;
        }
        return createPropertyAccessStrategy(className, propertyName, readMember, writeMember, usesFieldAccess);
    }
