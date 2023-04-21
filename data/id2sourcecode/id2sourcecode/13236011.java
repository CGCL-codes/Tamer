    private static boolean isBeanProperty(final PropertyDescriptor property) {
        Method readMethod = property.getReadMethod();
        if ((readMethod == null) && (property instanceof IndexedPropertyDescriptor)) {
            readMethod = ((IndexedPropertyDescriptor) property).getIndexedReadMethod();
        }
        Method writeMethod = property.getWriteMethod();
        if ((writeMethod == null) && (property instanceof IndexedPropertyDescriptor)) {
            writeMethod = ((IndexedPropertyDescriptor) property).getIndexedWriteMethod();
        }
        return readMethod != null && writeMethod != null;
    }
