    @SuppressWarnings("unchecked")
    private ReflectiveMetaProperty(Class<? extends Bean> beanType, String propertyName) {
        super(propertyName);
        PropertyDescriptor descriptor;
        try {
            descriptor = new PropertyDescriptor(propertyName, beanType);
        } catch (IntrospectionException ex) {
            throw new NoSuchFieldError("Invalid property: " + propertyName + ": " + ex.getMessage());
        }
        Method readMethod = descriptor.getReadMethod();
        Method writeMethod = descriptor.getWriteMethod();
        if (readMethod == null && writeMethod == null) {
            throw new NoSuchFieldError("Invalid property: " + propertyName + ": Both read and write methods are missing");
        }
        this.declaringType = (readMethod != null ? readMethod.getDeclaringClass() : writeMethod.getDeclaringClass());
        this.propertyType = (Class<P>) descriptor.getPropertyType();
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }
