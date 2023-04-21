    private Class determinePropertyType() {
        Method readMethod = getBridgedReadMethod();
        Method writeMethod = getBridgedWriteMethod();
        Class returnType = null;
        try {
            returnType = TypeResolver.resolvePropertyType(clazz, readMethod, writeMethod);
        } catch (Exception ignore) {
        }
        if (returnType != null) {
            return returnType;
        }
        if (readMethod == null && writeMethod == null) {
            throw new MappingException("No read or write method found for field (" + fieldName + ") in class (" + clazz + ")");
        }
        if (readMethod == null) {
            return determineByWriteMethod(writeMethod);
        } else {
            try {
                return readMethod.getReturnType();
            } catch (Exception e) {
                return determineByWriteMethod(writeMethod);
            }
        }
    }
