    public static PropertyInformation create(String name, boolean isReadable, boolean isWritable, Method readMethod, Method writeMethod, Class<?> readMethodReturnType, Class<?> writeMethodParameterType) {
        PropertyInformationBean propertyInformation = new PropertyInformationBean();
        propertyInformation.setName(name);
        propertyInformation.setReadable(isReadable);
        propertyInformation.setWritable(isWritable);
        propertyInformation.setReadMethod(readMethod);
        propertyInformation.setWriteMethod(writeMethod);
        propertyInformation.setReadMethodReturnType(readMethodReturnType);
        propertyInformation.setWriteMethodParameterType(writeMethodParameterType);
        return propertyInformation;
    }
