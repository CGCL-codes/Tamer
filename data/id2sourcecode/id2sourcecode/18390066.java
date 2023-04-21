    private boolean hasCorrespondingReadProperty(Method writeProperty, Class c, String readPropertyPrefix) {
        String writePropertyMethodName = writeProperty.getName();
        Class[] writePropertyParameters = writeProperty.getParameterTypes();
        boolean foundReadProperty = false;
        try {
            String readPropertyMethodName = writePropertyMethodName.replaceFirst(SET_PROPERTY_PREFIX, readPropertyPrefix);
            Method readPropertyMethod = c.getMethod(readPropertyMethodName, new Class[] {});
            foundReadProperty = (isPropertyAccessorMethod(readPropertyMethod, c) && (readPropertyMethod.getReturnType() == writePropertyParameters[0]));
        } catch (Exception e) {
        }
        return foundReadProperty;
    }
