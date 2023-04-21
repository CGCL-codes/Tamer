    private Class checkMethods(Method readMethod, Method writeMethod) throws IntrospectionException {
        Class newPropertyType = propertyType;
        if (readMethod != null) {
            if (readMethod.getParameterTypes().length > 0) {
                throw new IntrospectionException("read method has unexpected parameters");
            }
            newPropertyType = readMethod.getReturnType();
            if (newPropertyType == Void.TYPE) {
                throw new IntrospectionException("read method return type is void");
            }
        }
        if (writeMethod != null) {
            if (writeMethod.getParameterTypes().length != 1) {
                String msg = "write method does not have exactly one parameter";
                throw new IntrospectionException(msg);
            }
            if (readMethod == null) {
                newPropertyType = writeMethod.getParameterTypes()[0];
            } else {
                if (newPropertyType != null && !newPropertyType.isAssignableFrom(writeMethod.getParameterTypes()[0])) {
                    throw new IntrospectionException("read and write method are not compatible");
                }
            }
        }
        return newPropertyType;
    }
