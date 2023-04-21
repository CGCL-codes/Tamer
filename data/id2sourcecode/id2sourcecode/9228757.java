    public static void recreateCollections(Object obj, CopyReseter reseter) throws ApplicationException {
        if (obj == null) {
            return;
        }
        PropertyDescriptor[] props = ClassUtils.getBeanCollectionProperties(obj.getClass());
        for (int i = 0; i < props.length; i++) {
            PropertyDescriptor desc = props[i];
            Class type = desc.getPropertyType();
            Method readMethod = desc.getReadMethod();
            Method writeMethod = desc.getWriteMethod();
            if (readMethod == null || writeMethod == null) {
                continue;
            }
            Object value = ClassUtils.getBeanValue(readMethod, obj);
            if (!(value instanceof Collection)) {
                continue;
            }
            Collection collection = (Collection) value;
            resetCollection(collection, reseter);
            Object cloneCollection = cloneCollection(collection, type);
            ClassUtils.setBeanValue(writeMethod, obj, cloneCollection);
        }
    }
