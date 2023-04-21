    public static Node getBeanProperties(String typeName) throws Exception {
        try {
            Class<?> clazz = AnnotationsHelper.class.getClassLoader().loadClass(typeName);
            PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
            String out = "<properties>\n";
            for (PropertyDescriptor descriptor : descriptors) {
                String propertyType = "";
                if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
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
                out += "\t<property name=\"" + descriptor.getName() + "\" " + "type=\"" + propertyType + "\" " + "readMethod=\"" + descriptor.getReadMethod().getName() + "\" " + "writeMethod=\"" + descriptor.getWriteMethod().getName() + "\"/>\n";
            }
            out += "</properties>";
            return buildDocumentElement(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
