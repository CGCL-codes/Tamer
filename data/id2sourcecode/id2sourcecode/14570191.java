    private void findMappedPropertyType() throws IntrospectionException {
        try {
            Method mappedReadMethod = getMappedReadMethod();
            Method mappedWriteMethod = getMappedWriteMethod();
            Class mappedPropertyType = null;
            if (mappedReadMethod != null) {
                if (mappedReadMethod.getParameterTypes().length != 1) {
                    throw new IntrospectionException("bad mapped read method arg count");
                }
                mappedPropertyType = mappedReadMethod.getReturnType();
                if (mappedPropertyType == Void.TYPE) {
                    throw new IntrospectionException("mapped read method " + mappedReadMethod.getName() + " returns void");
                }
            }
            if (mappedWriteMethod != null) {
                Class[] params = mappedWriteMethod.getParameterTypes();
                if (params.length != 2) {
                    throw new IntrospectionException("bad mapped write method arg count");
                }
                if (mappedPropertyType != null && mappedPropertyType != params[1]) {
                    throw new IntrospectionException("type mismatch between mapped read and write methods");
                }
                mappedPropertyType = params[1];
            }
            mappedPropertyTypeRef = new SoftReference(mappedPropertyType);
        } catch (IntrospectionException ex) {
            throw ex;
        }
    }
