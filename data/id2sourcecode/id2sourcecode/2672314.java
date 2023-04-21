    public static Node getEntityIdBeanProperty(String typeName) throws Exception {
        try {
            Class<?> clazz = AnnotationsHelper.class.getClassLoader().loadClass(typeName);
            PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
            String out = "";
            for (PropertyDescriptor descriptor : descriptors) {
                String propertyType = "";
                if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
                    continue;
                }
                Annotation idAnnotation = getPropertyAnnotation(clazz, descriptor, javax.persistence.Id.class);
                if (idAnnotation == null) {
                    idAnnotation = getPropertyAnnotation(clazz, descriptor, javax.persistence.EmbeddedId.class);
                }
                if (idAnnotation == null) {
                    continue;
                }
                Type type = descriptor.getReadMethod().getGenericReturnType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType partype = (ParameterizedType) type;
                    Type rawType = partype.getRawType();
                    propertyType = ((Class<?>) rawType).getName() + "&lt;";
                    Type[] aTypeArgs = partype.getActualTypeArguments();
                    for (Type aTypeArg : aTypeArgs) {
                        propertyType += ((Class<?>) aTypeArg).getName();
                    }
                    propertyType += "&gt;";
                } else {
                    propertyType = getTypeName((Class<?>) type);
                }
                out += "<property name=\"" + descriptor.getName() + "\" " + "type=\"" + propertyType + "\" " + "readMethod=\"" + descriptor.getReadMethod().getName() + "\" " + "writeMethod=\"" + descriptor.getWriteMethod().getName() + "\"/>";
                return buildDocumentElement(out);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
