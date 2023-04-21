    protected Collection<JavaProperty> initProperties() {
        List<JavaProperty> properties = new ArrayList<JavaProperty>();
        for (PropertyDescriptor propertyDescriptor : getPropertyDescriptors(getType())) {
            String name = propertyDescriptor.getName();
            JavaMethod readMethod = null;
            JavaMethod writeMethod = null;
            Method method = propertyDescriptor.getReadMethod();
            if (method != null) readMethod = new JavaMethod(method, MethodType.GETTER);
            method = propertyDescriptor.getWriteMethod();
            if (method != null) writeMethod = new JavaMethod(method, MethodType.SETTER);
            if (readMethod != null || writeMethod != null) properties.add(new JavaMethodProperty(provider, name, readMethod, writeMethod));
        }
        return properties;
    }
