    protected PropertyDescriptor(ClassDescriptor classDescriptor, String propertyName, Method readMethod, Method writeMethod) {
        super();
        this.classDescriptor = this.mustNotBeNull("classDescriptor", classDescriptor);
        this.propertyName = mustNotBeNullOrEmpty("propertyName", propertyName);
        this.readMethod = this.mustNotBeNull("readMethod", readMethod);
        this.writeMethod = writeMethod;
    }
