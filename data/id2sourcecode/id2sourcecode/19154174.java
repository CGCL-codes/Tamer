    public static String copyProperties(Object fromObject, Object toObject) {
        if (fromObject == null || toObject == null) {
            return "Both source and destination objects must be not null";
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
            for (int i = 0; i < sizeOfToObjectProperties; i++) {
                PropertyDescriptor descriptorOfToObject = descriptorsOfToObject[i];
                Method writeMethod = descriptorOfToObject.getWriteMethod();
                if (writeMethod != null) {
                    for (int i1 = 0; i1 < sizeOfFromObjectProperties; i1++) {
                        PropertyDescriptor descriptorOfFromObject = descriptorsOfFromObject[i1];
                        if (descriptorOfFromObject.getName().equals(descriptorOfToObject.getName())) {
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
                }
            }
        } catch (IntrospectionException ie) {
            log.printStackTrace(ie, LogManager.WARN);
        }
        return "Object was successfully loaded.";
    }
