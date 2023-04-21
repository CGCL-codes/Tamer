    private void updateObject(DtoState dtoState, Object sourceObject, Object targetObject, boolean updateTarget, boolean deepCopy) {
        try {
            if (dtoState.classData == null) {
                dtoState.classData = getClassData(dtoState.domainClass, dtoState.dtoClass);
                if (dtoState.dtoClass == null) dtoState.dtoClass = dtoClassGenerator.getDtoClass(dtoState.domainClass, deepCopy);
            }
            for (int i = 0; i < dtoState.classData.targetProperties.length; i++) {
                PropertyDescriptor sourceProperty = dtoState.classData.sourceProperties[i];
                PropertyDescriptor targetProperty = dtoState.classData.targetProperties[i];
                try {
                    if ((sourceProperty == null) || (targetProperty == null)) continue;
                    Method readMethod = updateTarget ? sourceProperty.getReadMethod() : targetProperty.getReadMethod();
                    Method writeMethod = updateTarget ? targetProperty.getWriteMethod() : sourceProperty.getWriteMethod();
                    if ((readMethod == null) || (writeMethod == null)) continue;
                    Object[] readParams = new Object[0];
                    Object value = readMethod.invoke(updateTarget ? sourceObject : targetObject, readParams);
                    if (value instanceof DtoIdentifier) {
                        if (!deepCopy) value = ((DtoIdentifier) value).getId(); else {
                            DtoState dtoFieldState = new DtoState();
                            dtoFieldState.domainClass = value.getClass();
                            dtoFieldState.dtoClass = dtoClassGenerator.getDtoClass(dtoFieldState.domainClass, deepCopy);
                            dtoFieldState.classData = getClassData(dtoFieldState.domainClass, dtoFieldState.dtoClass);
                            Object dto = dtoFieldState.dtoClass.newInstance();
                            updateObject(dtoFieldState, value, dto, true, deepCopy);
                            value = dto;
                        }
                    }
                    Object[] writeParams = new Object[1];
                    writeParams[0] = value;
                    writeMethod.invoke(updateTarget ? targetObject : sourceObject, writeParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
