    private void initCascadeProcessing() {
        try {
            for (Field field : persistentClass.getDeclaredFields()) {
                CascadeWrite cascadeConfig = field.getAnnotation(CascadeWrite.class);
                if (cascadeConfig == null) {
                    continue;
                }
                Method getter = persistentClass.getMethod(getPropertyGetter(field.getName()));
                if (log.isDebugEnabled()) {
                    log.debug("CascadeWrite on field '" + field.getName() + "' using method '" + getter.getName() + "'");
                }
                cascades.put(getter, cascadeConfig);
            }
        } catch (SecurityException e) {
            throw new InvalidDataAccessApiUsageException("Could not read cascade write configuration", e);
        } catch (NoSuchMethodException e) {
            throw new InvalidDataAccessApiUsageException("Could not read cascade write configuration", e);
        }
    }
