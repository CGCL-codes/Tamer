    static void validateWritePipeTypes(final DataReader dtoRead, final DataWriter entityWrite) throws IllegalArgumentException {
        final Class<?> dtoReadClass = dtoRead.getReturnType();
        final Class<?> entityWriteClass = entityWrite.getParameterType();
        if (!dtoReadClass.isInterface() && dtoReadClass.getAnnotation(Dto.class) == null && !entityWriteClass.equals(Object.class) && !dtoReadClass.equals(Object.class) && !sameDataType(dtoReadClass, entityWriteClass)) {
            throw new IllegalArgumentException("Type mismatch is detected for: DTO read {" + dtoRead + "} and Entity write {" + entityWrite + "}. Consider using a converter.");
        }
    }
