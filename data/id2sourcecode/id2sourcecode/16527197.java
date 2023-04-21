    public void writeDestValue(Object runtimeDestObj, Object destFieldValue) {
        if (log.isDebugEnabled()) {
            String className = MappingUtils.getClassNameWithoutPackage(runtimeDestObj.getClass());
            log.debug("Getting ready to invoke write method on the destination object. Dest Obj: {}, Dest value: {}", className, destFieldValue);
        }
        DozerPropertyDescriptor propDescriptor = getDestPropertyDescriptor(runtimeDestObj.getClass());
        propDescriptor.setPropertyValue(runtimeDestObj, destFieldValue, this);
    }
