    private static MBeanAttributeInfo[] createAttributeInfo(Map<String, PropertyDescriptor> propertyDescriptors) throws ManagementException, IntrospectionException {
        MBeanAttributeInfo[] infos = new MBeanAttributeInfo[propertyDescriptors.size()];
        int i = 0;
        for (String propertyName : sortedKeys(propertyDescriptors)) {
            PropertyDescriptor property = propertyDescriptors.get(propertyName);
            boolean isAutomatic = isAutomatic(property);
            Method readMethod = property.getReadMethod();
            Method writeMethod = property.getWriteMethod();
            boolean readable = isAutomatic || (null != getAnnotation(readMethod, ManagedAttribute.class));
            boolean writable = isAutomatic || (null != getAnnotation(writeMethod, ManagedAttribute.class));
            Description descriptionAnnotation = getSingleAnnotation(property, Description.class, readMethod, writeMethod);
            String description = (descriptionAnnotation != null) ? descriptionAnnotation.value() : null;
            MBeanAttributeInfo info = new MBeanAttributeInfo(property.getName(), description, readable ? readMethod : null, writable ? writeMethod : null);
            infos[i++] = info;
        }
        return infos;
    }
