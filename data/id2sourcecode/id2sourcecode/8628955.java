    private ArrayObjectInfo introspectArray(Object array, Map<Object, NonNativeObjectInfo> alreadyReadObjects, ODBType valueType, IntrospectionCallback callback) {
        int length = OdbReflection.getArrayLength(array);
        Class elementType = array.getClass().getComponentType();
        ODBType type = ODBType.getFromClass(elementType);
        if (type.isAtomicNative()) {
            return intropectAtomicNativeArray(array, type);
        }
        AbstractObjectInfo[] arrayCopy = new AbstractObjectInfo[length];
        Collection<NonNativeObjectInfo> nonNativeObjects = new ArrayList<NonNativeObjectInfo>(length);
        for (int i = 0; i < length; i++) {
            Object o = OdbReflection.getArrayElement(array, i);
            ClassInfo ci = null;
            if (o != null) {
                AbstractObjectInfo aoi = getObjectInfoInternal(o, alreadyReadObjects, callback);
                arrayCopy[i] = aoi;
                if (aoi.isNonNativeObject()) {
                    nonNativeObjects.add((NonNativeObjectInfo) aoi);
                }
            } else {
                arrayCopy[i] = new NonNativeNullObjectInfo();
                nonNativeObjects.add((NonNativeObjectInfo) arrayCopy[i]);
            }
        }
        ArrayObjectInfo arrayOfAoi = new ArrayObjectInfo(arrayCopy, valueType, type.getId());
        arrayOfAoi.setNonNativeObjects(nonNativeObjects);
        return arrayOfAoi;
    }
