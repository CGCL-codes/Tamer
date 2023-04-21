    public static PropertyDescriptor getPropertyDescriptor2(final Class<?> beanClazz, final String property) {
        final Indexed propertiesIndex = getPropertiesIndex(property);
        try {
            if (propertiesIndex != null) {
                return new IndexedPropertyDescriptor(propertiesIndex.propertyName, beanClazz);
            }
            final BeanInfo beanInfo = Introspector.getBeanInfo(beanClazz);
            final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (final PropertyDescriptor propertyDescriptor : descriptors) {
                if (property.equals(propertyDescriptor.getName())) {
                    return propertyDescriptor;
                }
            }
        } catch (final IntrospectionException e1) {
            System.err.println("");
        }
        Method readMethod = null;
        Method writeMethod = null;
        try {
            final String property2 = Character.toUpperCase(property.charAt(0)) + property.substring(1);
            readMethod = beanClazz.getMethod("get" + property2, new Class[0]);
            writeMethod = ReflectionUtil.getMethod(beanClazz, "set" + property2, new Class[] { readMethod.getReturnType() });
        } catch (final Throwable e) {
        }
        try {
            final PropertyDescriptor result = new PropertyDescriptor(property, readMethod, writeMethod);
            return result;
        } catch (final IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }
