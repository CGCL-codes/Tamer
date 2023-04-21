    static int getRecordLength(GenericBean instance) throws IOException {
        int result = 0;
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(instance.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field = readMethod.invoke(instance);
                    result += getFieldLength(field, descriptor);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return result;
    }
