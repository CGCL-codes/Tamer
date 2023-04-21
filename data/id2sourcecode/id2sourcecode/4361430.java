    @SuppressWarnings({ "unchecked" })
    public void copyTo(T destination) {
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field = readMethod.invoke(this);
                    final Class<?> returnType = readMethod.getReturnType();
                    if (returnType.isAnnotationPresent(GenericBeanInfo.class)) {
                        final GenericBean otherField = (GenericBean) readMethod.invoke(destination);
                        ((GenericBean) field).copyTo(otherField);
                    } else {
                        writeMethod.invoke(destination, field);
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
