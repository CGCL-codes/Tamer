    public MethodAccessor(IPropertyDescription propertyDescriptor, Method readMethod, Method writeMethod) {
        super(propertyDescriptor);
        this.readMethod = this.mustNotBeNull("readMethod", readMethod);
        this.writeMethod = writeMethod;
    }
