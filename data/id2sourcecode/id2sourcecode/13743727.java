    public PropertyRef(Object bean, String propertyName, Class<PropertyType> propertyType, Method readMethod, Method writeMethod, Object... parameters) {
        if (propertyType == null) throw new NullPointerException("propertyType");
        if (parameters != null && parameters.length == 0) parameters = null;
        this.bean = bean;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.parameters = parameters;
    }
