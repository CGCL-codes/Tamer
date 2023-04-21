    protected void checkProperty(PojoDescriptor<?> pojoDescriptor, String propertyName, Class<?> readType, Class<?> writeType) {
        PojoPropertyDescriptor propertyDescriptor = pojoDescriptor.getPropertyDescriptor(propertyName);
        assertNotNull(propertyDescriptor);
        assertEquals(propertyName, propertyDescriptor.getName());
        PojoPropertyAccessorNonArg getAccessor = propertyDescriptor.getAccessor(PojoPropertyAccessorNonArgMode.GET);
        if (readType == null) {
            assertNull(getAccessor);
        } else {
            assertNotNull(getAccessor);
            assertEquals(propertyName, getAccessor.getName());
            assertEquals(readType, getAccessor.getPropertyClass());
            assertEquals(readType, getAccessor.getPropertyType().getRetrievalClass());
            assertEquals(getAccessor.getPropertyType(), getAccessor.getReturnType());
            assertSame(getAccessor.getPropertyClass(), getAccessor.getReturnClass());
        }
        PojoPropertyAccessorOneArg setAccessor = propertyDescriptor.getAccessor(PojoPropertyAccessorOneArgMode.SET);
        if (writeType == null) {
            assertNull(setAccessor);
        } else {
            assertNotNull(setAccessor);
            assertEquals(propertyName, setAccessor.getName());
            assertEquals(writeType, setAccessor.getPropertyClass());
            assertEquals(writeType, setAccessor.getPropertyType().getAssignmentClass());
        }
    }
