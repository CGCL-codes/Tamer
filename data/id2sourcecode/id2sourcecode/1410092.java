    public PropertyDescriptor(String name, Method readMethod, Method writeMethod) throws IntrospectionException {
        setName(name);
        getMethod = readMethod;
        setMethod = writeMethod;
        propertyType = checkMethods(getMethod, setMethod);
    }
