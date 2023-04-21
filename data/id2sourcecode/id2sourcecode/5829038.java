    public void addParameter(String name, String displayName, Class<?> paramClass, Object val, Method readMethod, Method writeMethod) {
        addParameter(name, paramClass, val, readMethod, writeMethod);
        nameMap.put(name, displayName);
    }
