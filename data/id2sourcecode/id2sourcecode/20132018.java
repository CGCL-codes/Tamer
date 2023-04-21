    public Type[] getGenericTypes() {
        Type type = readMethod != null ? readMethod.getMember().getGenericReturnType() : writeMethod.getMember().getGenericParameterTypes()[0];
        if (!(type instanceof ParameterizedType)) return null;
        return ((ParameterizedType) type).getActualTypeArguments();
    }
