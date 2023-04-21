    public static List getFullProperties(Object obj) throws Exception {
        List list = new ArrayList();
        BeanInfo desc = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor props[] = desc.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++) {
            Method writeMethod = props[i].getWriteMethod();
            Method readMethod = props[i].getReadMethod();
            if (writeMethod != null && readMethod != null) list.add(props[i]);
        }
        return list;
    }
