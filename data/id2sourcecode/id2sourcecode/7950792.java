    public Element encode(Object object) throws EncodingException {
        Element root = super.encode(object);
        if (object != null) {
            Class<?> clazz = object.getClass();
            root.setAttribute("type", clazz.getName());
            PropertyDescriptor[] propertyDescriptors;
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
                propertyDescriptors = beanInfo.getPropertyDescriptors();
            } catch (IntrospectionException e) {
                throw new EncodingException("Failed to introspect object: " + e.getMessage());
            }
            EncodingFactory encodingFactory = EncodingFactory.getInstance();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                Method readMethod = descriptor.getReadMethod();
                Method writeMethod = descriptor.getReadMethod();
                if (readMethod == null || writeMethod == null) {
                    continue;
                }
                Object value;
                try {
                    value = readMethod.invoke(object, new Object[] {});
                } catch (Exception e) {
                    throw new EncodingException("Failed to invoke get method: " + e.getMessage());
                }
                Element element = encodingFactory.encode(value);
                element.setAttribute("name", descriptor.getName());
                root.addContent(element);
            }
        }
        return root;
    }
