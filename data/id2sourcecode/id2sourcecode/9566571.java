    public AbstractMapper(Method readMethod, Method writeMethod, boolean findPropertyEditor) throws OdmException {
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        Class<?> clazz = readMethod.getReturnType();
        if (clazz.isArray()) {
            type = TYPE.ARRAY;
            coreClass = clazz.getComponentType();
            if (findPropertyEditor) {
                PropertyEditor propertyEditor = PropertyEditorManager.findEditor(coreClass);
                if (propertyEditor != null) {
                    propertyEditorClass = propertyEditor.getClass();
                }
            }
        } else if (Collection.class.isAssignableFrom(clazz)) {
            type = TYPE.COLLECTION;
            if (clazz.isInterface()) {
                if (List.class.isAssignableFrom(clazz)) {
                    collectionClass = ArrayList.class;
                } else if (Set.class.isAssignableFrom(clazz)) {
                    collectionClass = HashSet.class;
                } else {
                    throw new OdmException("Type not supported: " + clazz.getName());
                }
            } else {
                collectionClass = clazz;
            }
            Type returnType = readMethod.getGenericReturnType();
            if (returnType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) returnType;
                coreClass = (Class<?>) pt.getActualTypeArguments()[0];
                if (findPropertyEditor) {
                    PropertyEditor propertyEditor = PropertyEditorManager.findEditor(coreClass);
                    if (propertyEditor != null) {
                        propertyEditorClass = propertyEditor.getClass();
                    }
                }
            } else {
                throw new OdmException("Collections must declare a generic type");
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            throw new OdmException("Maps are not supported");
        } else {
            type = TYPE.OTHER;
            coreClass = clazz;
            if (findPropertyEditor) {
                PropertyEditor propertyEditor = PropertyEditorManager.findEditor(coreClass);
                if (propertyEditor != null) {
                    propertyEditorClass = propertyEditor.getClass();
                }
            }
        }
        if (propertyEditorClass == null && findPropertyEditor) {
            throw new OdmException("No suitable propertyeditor can be found for " + coreClass);
        }
    }
