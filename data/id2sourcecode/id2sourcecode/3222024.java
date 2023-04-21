    private void createArrayCopy(final Object sourceArray, final Object arrayCopy, final ClassInfoFactory classInfoFactory, final HelperBinaryConversion helperBinaryConversion) throws CopierException {
        try {
            final int arrayLength = Array.getLength(sourceArray);
            final Class<?> componentType = sourceArray.getClass().getComponentType();
            if (componentType.equals(StringBuilder.class) || haveConverter(componentType, classInfoFactory, helperBinaryConversion)) {
                for (int index = 0; index < arrayLength; index++) {
                    final Object elementObject = helperReflect.getArrayElement(sourceArray, index);
                    helperReflect.setArrayElement(arrayCopy, index, elementObject);
                }
            } else {
                for (int index = 0; index < arrayLength; index++) {
                    final Object elementObject = helperReflect.getArrayElement(sourceArray, index);
                    addObjectToCopy(elementObject);
                    final Object elemetCopy = getObjectCopy(elementObject, classInfoFactory);
                    helperReflect.setArrayElement(arrayCopy, index, elemetCopy);
                }
            }
        } catch (ReflectException exception) {
            throw new CopierException(exception);
        } catch (ReflectFailedSetException exception) {
            throw new CopierException(exception);
        }
    }
