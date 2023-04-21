    public void addParameter(String name, Class<?> paramClass, Object val, Method readMethod, Method writeMethod) {
        super.addParameter(name, paramClass, val, writeMethod == null);
        FastClass fastClass = FastClass.create(readMethod.getDeclaringClass());
        FastMethod fReadMethod = fastClass.getMethod(readMethod);
        FastMethod fWriteMethod = writeMethod == null ? null : fastClass.getMethod(writeMethod);
        bindMap.put(name, new ObjectParameter(fReadMethod, fWriteMethod));
    }
