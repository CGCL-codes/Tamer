    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenericBean that = (GenericBean) o;
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field_1 = readMethod.invoke(this);
                    final Object field_2 = readMethod.invoke(that);
                    if (readMethod.getReturnType().isArray()) {
                        if (!Arrays.deepEquals(new Object[] { field_1 }, new Object[] { field_2 })) {
                            return false;
                        }
                    } else {
                        if (!field_1.equals(field_2)) return false;
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return true;
    }
