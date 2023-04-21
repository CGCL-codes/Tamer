    private BeanProperty(String name, Method readMethod, Method writeMethod) {
        if (readMethod == null && writeMethod == null) throw new IllegalArgumentException("Invalid property " + name + ": missing at least one accessor method");
        if (readMethod != null && writeMethod != null && !readMethod.getReturnType().equals(writeMethod.getParameterTypes()[0])) throw new IllegalArgumentException("return type differs: " + readMethod.getReturnType() + " and " + writeMethod.getParameterTypes()[0]);
        this.name = name;
        this.type = (Class<T>) (readMethod != null ? readMethod.getReturnType() : writeMethod.getParameterTypes()[0]);
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }
