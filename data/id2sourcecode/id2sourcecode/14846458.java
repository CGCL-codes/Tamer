    public PropertyAdaptor(String propertyName, Method readMethod, Method writeMethod) {
        this.propertyName = propertyName;
        this.getter = readMethod;
        this.setter = writeMethod;
    }
