    private void arrayCopy(final Object sourceArray, final Object arrayCopy) throws CopierException {
        final int arrayLength = Array.getLength(sourceArray);
        final Class<?> componentType = sourceArray.getClass().getComponentType();
        ClassInfo componentTypeClassInfo;
        try {
            componentTypeClassInfo = classInfoFactory.getNoProxyClassInfo(componentType);
        } catch (ClassInfoException exception) {
            throw new CopierException(exception);
        }
        if (componentTypeClassInfo.isStringType() || componentTypeClassInfo.isBasicType()) {
            for (int index = 0; index < arrayLength; index++) {
                final Object elementObject;
                try {
                    elementObject = HELPER_REFLECT.getArrayElement(sourceArray, index);
                } catch (ReflectException exception) {
                    throw new CopierException("element of " + identityString(sourceArray), exception);
                }
                try {
                    HELPER_REFLECT.setArrayElement(arrayCopy, index, elementObject);
                } catch (ReflectException exception) {
                    throw new CopierException(ELEMENT + elementObject.getClass() + OF + identityString(sourceArray), exception);
                } catch (ReflectFailedSetException exception) {
                    throw new CopierException(ELEMENT + elementObject.getClass() + OF + identityString(sourceArray), exception);
                }
            }
        } else {
            for (int index = 0; index < arrayLength; index++) {
                Object elementObject;
                try {
                    elementObject = HELPER_REFLECT.getArrayElement(sourceArray, index);
                } catch (ReflectException exception) {
                    throw new CopierException("element of " + sourceArray, exception);
                }
                addObjectToCopyQueue(elementObject);
                final Object elemetCopy;
                if (elementObject == null) {
                    elemetCopy = elementObject;
                } else {
                    ClassInfo elementObjectClassInfo;
                    try {
                        elementObjectClassInfo = classInfoFactory.getNoProxyClassInfo(elementObject.getClass());
                    } catch (ClassInfoException exception) {
                        throw new CopierException(exception);
                    }
                    if (elementObjectClassInfo.isStringType() || elementObjectClassInfo.isBasicType()) {
                        elemetCopy = elementObject;
                    } else {
                        try {
                            elemetCopy = getOrCreateObjectCopy(elementObject, elementObjectClassInfo);
                        } catch (CopierException exception) {
                            throw new CopierException(ELEMENT + elementObject.getClass() + OF + sourceArray, exception);
                        }
                    }
                }
                try {
                    HELPER_REFLECT.setArrayElement(arrayCopy, index, elemetCopy);
                } catch (ReflectException exception) {
                    throw new CopierException(ELEMENT + (elementObject == null ? NULL : elementObject.getClass().toString()) + OF + sourceArray, exception);
                } catch (ReflectFailedSetException exception) {
                    throw new CopierException(ELEMENT + (elementObject == null ? NULL : elementObject.getClass().toString()) + OF + sourceArray, exception);
                }
            }
        }
    }
