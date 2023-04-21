    static void validateReadPipeTypes(final DataWriter dtoWrite, final DataReader entityRead) throws IllegalArgumentException {
        final Class<?> dtoWriteClass = dtoWrite.getParameterType();
        final Class<?> entityReadClass = entityRead.getReturnType();
        if (!dtoWriteClass.isInterface() && dtoWriteClass.getAnnotation(Dto.class) == null && !entityReadClass.equals(Object.class) && !dtoWriteClass.equals(Object.class) && !sameDataType(dtoWriteClass, entityReadClass)) {
            throw new IllegalArgumentException("Type mismatch is detected for: DTO write {" + dtoWrite + "}{" + dtoWriteClass + "} and Entity read {" + entityRead + "}{" + entityReadClass + "}. Consider using a converter.");
        }
    }
