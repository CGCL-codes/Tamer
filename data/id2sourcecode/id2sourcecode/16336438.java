    private void setPropertyValue(PropertyTokenHolder tokens, Object newValue) throws BeansException {
        String propertyName = tokens.canonicalName;
        if (tokens.keys != null) {
            PropertyTokenHolder getterTokens = new PropertyTokenHolder();
            getterTokens.canonicalName = tokens.canonicalName;
            getterTokens.actualName = tokens.actualName;
            getterTokens.keys = new String[tokens.keys.length - 1];
            System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);
            Object propValue = null;
            try {
                propValue = getPropertyValue(getterTokens);
            } catch (NotReadablePropertyException ex) {
                throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value in property referenced " + "in indexed property path '" + propertyName + "'", ex);
            }
            String key = tokens.keys[tokens.keys.length - 1];
            if (propValue == null) {
                throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value in property referenced " + "in indexed property path '" + propertyName + "': returned null");
            } else if (propValue.getClass().isArray()) {
                Class requiredType = propValue.getClass().getComponentType();
                int arrayIndex = Integer.parseInt(key);
                Object oldValue = null;
                try {
                    if (this.extractOldValueForEditor) {
                        oldValue = Array.get(propValue, arrayIndex);
                    }
                    Object convertedValue = doTypeConversionIfNecessary(propertyName, propertyName, oldValue, newValue, requiredType);
                    Array.set(propValue, Integer.parseInt(key), convertedValue);
                } catch (IllegalArgumentException ex) {
                    PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
                    throw new TypeMismatchException(pce, requiredType, ex);
                } catch (IndexOutOfBoundsException ex) {
                    throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Invalid array index in property path '" + propertyName + "'", ex);
                }
            } else if (propValue instanceof List) {
                List list = (List) propValue;
                int index = Integer.parseInt(key);
                Object oldValue = null;
                if (this.extractOldValueForEditor && index < list.size()) {
                    oldValue = list.get(index);
                }
                Object convertedValue = doTypeConversionIfNecessary(propertyName, propertyName, oldValue, newValue, null);
                if (index < list.size()) {
                    list.set(index, convertedValue);
                } else if (index >= list.size()) {
                    for (int i = list.size(); i < index; i++) {
                        try {
                            list.add(null);
                        } catch (NullPointerException ex) {
                            throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Cannot set element with index " + index + " in List of size " + list.size() + ", accessed using property path '" + propertyName + "': List does not support filling up gaps with null elements");
                        }
                    }
                    list.add(convertedValue);
                }
            } else if (propValue instanceof Map) {
                Map map = (Map) propValue;
                Object oldValue = null;
                if (this.extractOldValueForEditor) {
                    oldValue = map.get(key);
                }
                Object convertedValue = doTypeConversionIfNecessary(propertyName, propertyName, oldValue, newValue, null);
                map.put(key, convertedValue);
            } else {
                throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Property referenced in indexed property path '" + propertyName + "' is neither an array nor a List nor a Map; returned value was [" + newValue + "]");
            }
        } else {
            PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
            if (pd == null || pd.getWriteMethod() == null) {
                throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName);
            }
            Method readMethod = pd.getReadMethod();
            Method writeMethod = pd.getWriteMethod();
            Object oldValue = null;
            if (this.extractOldValueForEditor && readMethod != null) {
                try {
                    oldValue = readMethod.invoke(this.object, new Object[0]);
                } catch (Exception ex) {
                    logger.debug("Could not read previous value of property '" + this.nestedPath + propertyName, ex);
                }
            }
            try {
                Object convertedValue = doTypeConversionIfNecessary(propertyName, propertyName, oldValue, newValue, pd.getPropertyType());
                if (pd.getPropertyType().isPrimitive() && (convertedValue == null || "".equals(convertedValue))) {
                    throw new IllegalArgumentException("Invalid value [" + newValue + "] for property '" + pd.getName() + "' of primitive type [" + pd.getPropertyType() + "]");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("About to invoke write method [" + writeMethod + "] on object of class [" + this.object.getClass().getName() + "]");
                }
                writeMethod.invoke(this.object, new Object[] { convertedValue });
                if (logger.isDebugEnabled()) {
                    logger.debug("Invoked write method [" + writeMethod + "] with value of type [" + pd.getPropertyType().getName() + "]");
                }
            } catch (InvocationTargetException ex) {
                PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
                if (ex.getTargetException() instanceof ClassCastException) {
                    throw new TypeMismatchException(propertyChangeEvent, pd.getPropertyType(), ex.getTargetException());
                } else {
                    throw new MethodInvocationException(propertyChangeEvent, ex.getTargetException());
                }
            } catch (IllegalArgumentException ex) {
                PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
                throw new TypeMismatchException(pce, pd.getPropertyType(), ex);
            } catch (IllegalAccessException ex) {
                PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
                throw new MethodInvocationException(pce, ex);
            }
        }
    }
