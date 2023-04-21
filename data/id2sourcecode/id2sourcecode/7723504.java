    public HashMap getLwProperties(final String className) {
        if (myCache.containsKey(className)) {
            return (HashMap) myCache.get(className);
        }
        if (Utils.validateJComponentClass(myLoader, className, false) != null) {
            return null;
        }
        final Class aClass;
        try {
            aClass = Class.forName(className, false, myLoader);
        } catch (final ClassNotFoundException exc) {
            throw new RuntimeException(exc.toString());
        }
        final BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(aClass);
        } catch (Throwable e) {
            return null;
        }
        final HashMap result = new HashMap();
        final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            final PropertyDescriptor descriptor = descriptors[i];
            final Method readMethod = descriptor.getReadMethod();
            final Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod == null || readMethod == null) {
                continue;
            }
            final String name = descriptor.getName();
            final LwIntrospectedProperty property = propertyFromClass(descriptor.getPropertyType(), name);
            if (property != null) {
                property.setDeclaringClassName(descriptor.getReadMethod().getDeclaringClass().getName());
                result.put(name, property);
            }
        }
        myCache.put(className, result);
        return result;
    }
