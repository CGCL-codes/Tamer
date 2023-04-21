    private Class<?> getPropertyDefiningClass(PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null) {
            Class<?> readMethodClass = propertyDescriptor.getReadMethod().getDeclaringClass();
            Class<?> writeMethodClass = propertyDescriptor.getWriteMethod().getDeclaringClass();
            if (readMethodClass.isAssignableFrom(writeMethodClass)) return writeMethodClass; else return readMethodClass;
        } else if (propertyDescriptor.getReadMethod() != null) return propertyDescriptor.getReadMethod().getDeclaringClass(); else return propertyDescriptor.getWriteMethod().getDeclaringClass();
    }
