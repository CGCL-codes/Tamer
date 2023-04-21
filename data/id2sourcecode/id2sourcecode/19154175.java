    public static String copyProperties(Object fromObject, Object toObject, String[] propertyNames) {
        if (fromObject == null || toObject == null) {
            return "Both source and destination objects must be not null";
        }
        if (propertyNames == null || propertyNames.length == 0) {
            return "No properties specified.";
        }
        try {
            BeanInfo beanInfoOfFromObject = Introspector.getBeanInfo(fromObject.getClass());
            PropertyDescriptor[] descriptorsOfFromObject = beanInfoOfFromObject.getPropertyDescriptors();
            if (descriptorsOfFromObject.length < 1) {
                return "FromObject has no readable properties";
            }
            int sizeOfFromObjectProperties = descriptorsOfFromObject.length;
            BeanInfo beanInfo = Introspector.getBeanInfo(toObject.getClass());
            PropertyDescriptor[] descriptorsOfToObject = beanInfo.getPropertyDescriptors();
            if (descriptorsOfToObject.length < 1) {
                return "ToObject has no writable properties";
            }
            int sizeOfToObjectProperties = descriptorsOfToObject.length;
            for (int p = 0; p < propertyNames.length; p++) {
                String specifiedPropertyName = propertyNames[p];
                for (int t = 0; t < sizeOfToObjectProperties; t++) {
                    PropertyDescriptor descriptorOfToObject = descriptorsOfToObject[t];
                    if (descriptorOfToObject.getName().equals(specifiedPropertyName)) {
                        Method writeMethod = descriptorOfToObject.getWriteMethod();
                        if (writeMethod != null) {
                            try {
                                for (int i = 0; i < sizeOfFromObjectProperties; i++) {
                                    PropertyDescriptor descriptorOfFromObject = descriptorsOfFromObject[i];
                                    if (descriptorOfFromObject.getName().equals(specifiedPropertyName)) {
                                        Method readMethod = descriptorOfFromObject.getReadMethod();
                                        if (readMethod != null) {
                                            try {
                                                Object rObj = readMethod.invoke(fromObject, null);
                                                writeMethod.invoke(toObject, new Object[] { rObj });
                                            } catch (InvocationTargetException ite) {
                                                log.warn(toObject.getClass().getName() + ".copyProperties():" + ite.toString());
                                            } catch (IllegalAccessException iae) {
                                                log.warn(toObject.getClass().getName() + ".copyProperties():" + iae.toString());
                                            } catch (IllegalArgumentException iae) {
                                                log.warn(toObject.getClass().getName() + ".copyProperties():The number of actual parameters supplied (by readMethod of fromObject) via args[" + readMethod.toString() + "] is different from the number of formal parameters required by the underlying method (of toObject's writeMethod):" + iae.toString());
                                            }
                                        }
                                        break;
                                    }
                                }
                            } catch (SecurityException se) {
                                log.warn("Property [" + specifiedPropertyName + "] throws SecurityException in toObject [" + toObject.getClass().getName() + "]");
                            }
                        }
                    }
                }
            }
        } catch (IntrospectionException ie) {
            log.printStackTrace(ie, LogManager.ERROR);
        }
        return "Object was successfully loaded.";
    }
