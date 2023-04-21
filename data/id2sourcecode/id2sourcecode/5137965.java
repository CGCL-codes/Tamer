    private void readBeanDescription(final Class className, final boolean init) {
        try {
            this.properties = new HashMap();
            final BeanInfo bi = Introspector.getBeanInfo(className);
            final PropertyDescriptor[] propertyDescriptors = bi.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                final PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                final Method readMethod = propertyDescriptor.getReadMethod();
                final Method writeMethod = propertyDescriptor.getWriteMethod();
                if (isValidMethod(readMethod, 0) && isValidMethod(writeMethod, 1)) {
                    final String name = propertyDescriptor.getName();
                    this.properties.put(name, propertyDescriptor);
                    if (init) {
                        super.setParameterDefinition(name, propertyDescriptor.getPropertyType());
                    }
                }
            }
        } catch (IntrospectionException e) {
            Log.error("Unable to build bean description", e);
        }
    }
