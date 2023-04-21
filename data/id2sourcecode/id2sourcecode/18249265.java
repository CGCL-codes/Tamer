    protected PropertyInfo(Class<?> ownerClass, Class<?> propType, String propName, Method readMethod, Method writeMethod) {
        super();
        this.ownerClass = ownerClass;
        this.propType = propType;
        this.propName = propName;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        if (writeMethod != null) this.propGenericType = writeMethod.getGenericParameterTypes()[0]; else this.propGenericType = propType;
    }
