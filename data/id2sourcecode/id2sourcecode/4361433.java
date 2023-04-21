    public int hashCode() {
        int result = 0;
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field = readMethod.invoke(this);
                    result = 29 * result + field.hashCode();
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
        return result;
    }
