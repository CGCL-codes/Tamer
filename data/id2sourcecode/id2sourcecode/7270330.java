    public PropertyDescriptor[] getBeanPrimitiveProperties(Class bean) {
        List properties = new ArrayList();
        PropertyDescriptor[] props = getPropertyDescriptors(bean);
        if (props != null) for (int i = 0; i < props.length; i++) {
            PropertyDescriptor prop = props[i];
            if (isAPrimitive(prop.getPropertyType())) {
                Method write = prop.getWriteMethod();
                Method read = prop.getReadMethod();
                if (write != null && read != null) {
                    Class[] parameterTypes = write.getParameterTypes();
                    Class returnType = read.getReturnType();
                    if (parameterTypes.length == 1 && parameterTypes[0].equals(returnType)) {
                        properties.add(prop);
                    }
                }
            }
        }
        return (PropertyDescriptor[]) properties.toArray(new PropertyDescriptor[0]);
    }
