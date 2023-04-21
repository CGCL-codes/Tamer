    private static boolean isBeanProperty(final PropertyDescriptor desc) {
        Method readMethod = desc.getReadMethod();
        if ((readMethod == null) && (desc instanceof IndexedPropertyDescriptor)) {
            readMethod = ((IndexedPropertyDescriptor) desc).getIndexedReadMethod();
        }
        Method writeMethod = desc.getWriteMethod();
        if ((writeMethod == null) && (desc instanceof IndexedPropertyDescriptor)) {
            writeMethod = ((IndexedPropertyDescriptor) desc).getIndexedWriteMethod();
        }
        return readMethod != null && writeMethod != null;
    }
