    @SuppressWarnings("unchecked")
    public void wrapWithMocks(Object objectToMockWrap) {
        List<String> writableProperties = PropertyUtils.getWriteableProperties(objectToMockWrap);
        for (String prop : writableProperties) {
            PropertyAdaptor adaptor = PropertyUtils.getPropertyAdaptor(objectToMockWrap, prop);
            Class<?> interfaceClass = adaptor.getPropertyType();
            if (adaptor.isReadable() && isMockable(interfaceClass)) {
                adaptor.write(objectToMockWrap, getServiceToUse(interfaceClass, adaptor.read(objectToMockWrap), true));
            }
        }
    }
