    protected SortedMap<String, JavaProperty> initProperties() {
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(type);
        SortedMap<String, JavaProperty> propertyMap = new TreeMap<String, JavaProperty>();
        for (Field field : type.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers()) && !"jdoDetachedState".equals(field.getName()) && !field.isAnnotationPresent(IgnoredProperty.class)) {
                String name = field.getName();
                JavaMethod readMethod = null;
                JavaMethod writeMethod = null;
                if (field.getType().isMemberClass() && !field.getType().isEnum()) throw new UnsupportedOperationException("Inner classes are not supported (except enums): " + field.getType());
                if (propertyDescriptors != null) {
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        if (name.equals(propertyDescriptor.getName())) {
                            if (propertyDescriptor.getReadMethod() != null) readMethod = new JavaMethod(propertyDescriptor.getReadMethod(), MethodType.GETTER);
                            if (propertyDescriptor.getWriteMethod() != null) writeMethod = new JavaMethod(propertyDescriptor.getWriteMethod(), MethodType.SETTER);
                            break;
                        }
                    }
                }
                JavaFieldProperty property = new JavaFieldProperty(provider, field, readMethod, writeMethod);
                propertyMap.put(name, property);
            }
        }
        if (propertyDescriptors != null) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getReadMethod().getDeclaringClass().equals(type) && propertyDescriptor.getReadMethod().isAnnotationPresent(ExternalizedProperty.class) && !propertyMap.containsKey(propertyDescriptor.getName())) {
                    JavaMethod readMethod = new JavaMethod(propertyDescriptor.getReadMethod(), MethodType.GETTER);
                    JavaMethodProperty property = new JavaMethodProperty(provider, propertyDescriptor.getName(), readMethod, null);
                    propertyMap.put(propertyDescriptor.getName(), property);
                }
            }
        }
        return propertyMap;
    }
