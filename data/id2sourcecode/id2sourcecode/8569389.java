    protected boolean doPopulate(Object source, Object target, Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {
        if (source instanceof Collection || target instanceof Collection || source instanceof Map || target instanceof Map || source instanceof ResultSet || target instanceof ResultSet || source instanceof ServletRequest || target instanceof ServletRequest) {
            return false;
        }
        PropertyDescriptor[] sourceDescriptors = null;
        PropertyDescriptor[] targetDescriptors = null;
        try {
            sourceDescriptors = Introspector.getBeanInfo(source.getClass()).getPropertyDescriptors();
            targetDescriptors = Introspector.getBeanInfo(target.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException ie) {
            logger.debug("Failed on getting bean's properties", ie);
            return false;
        }
        for (PropertyDescriptor targetDescriptor : targetDescriptors) {
            if (targetDescriptor.getName().equals("class") || !doProcess(targetDescriptor.getName(), ignoreProperties)) {
                continue;
            }
            PropertyDescriptor sourceDescriptor = getSourceDescriptor(propertiesMapping, targetDescriptor, sourceDescriptors);
            if (sourceDescriptor == null) {
                continue;
            }
            try {
                Method readMethod = sourceDescriptor.getReadMethod();
                Method writeMethod = targetDescriptor.getWriteMethod();
                if (readMethod == null || writeMethod == null) {
                    continue;
                }
                Object sourceValue = readMethod.invoke(source, new Object[0]);
                Object convertedValue = getConverter().convertValue(sourceValue, targetDescriptor.getPropertyType(), params);
                writeMethod.invoke(target, new Object[] { convertedValue });
            } catch (Exception e) {
                logger.debug("Exception", e);
            }
        }
        return true;
    }
