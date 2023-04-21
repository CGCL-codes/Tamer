    private void simplePropertiesTest(Object bean, BeanInfo beanInfo) throws Exception {
        PropertyDescriptor propertyDescriptors[] = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String name = descriptor.getName();
            if (excludedProperties.contains(name)) {
                continue;
            }
            Method readMethod = descriptor.getReadMethod();
            Method writeMethod = descriptor.getWriteMethod();
            if (readMethod != null && writeMethod != null) {
                simplePropertyTest(bean, descriptor, readMethod, writeMethod);
            }
        }
    }
