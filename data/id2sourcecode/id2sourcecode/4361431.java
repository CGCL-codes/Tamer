    public String toString() {
        StringBuffer result = new StringBuffer();
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && readMethod.isAnnotationPresent(AttrInfo.class)) {
                    final Object field = readMethod.invoke(this);
                    if (result.length() > 0) result.append(',');
                    result.append(descriptor.getDisplayName()).append('=');
                    appendField(result, field);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
