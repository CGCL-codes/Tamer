    @SuppressWarnings("unchecked")
    private <T> void addGetSetProperty(ClassPool classPool, String propertyName, Method readMethod, Class<?> readType, Method writeMethod, Class<?> writeType) {
        Class<?> theType = null;
        if (readType != null && writeType != null) {
            if (readType.isAssignableFrom(writeType)) theType = writeType; else if (writeType.isAssignableFrom(readType)) theType = writeType; else throw new IllegalArgumentException("prop: " + propertyName + " " + readType + " - is not compatible with:" + writeType);
        } else if (readType != null) {
            theType = readType;
        } else if (writeType != null) {
            theType = writeType;
        } else {
            throw new NullPointerException();
        }
        GetSetProperty<C, T> property = (GetSetProperty<C, T>) ivProperties.get(propertyName);
        if (property == null) {
            try {
                property = createGetSetProperty(classPool, this, ivClass, readMethod, writeMethod, theType, propertyName);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        if (readMethod != null) property.setAnnotatedMethod(readMethod);
        ivProperties.put(propertyName, property);
    }
