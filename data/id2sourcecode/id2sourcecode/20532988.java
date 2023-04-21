    public static boolean includeProperty(Class<?> javaClass, PropertyDescriptor desc) {
        Method reader = desc.getReadMethod();
        Method writer = desc.getWriteMethod();
        if (reader == null || writer == null) return false;
        if (reader.getAnnotation(HGIgnore.class) != null || writer.getAnnotation(HGIgnore.class) != null) return false;
        if (desc.getName().equals("atomHandle") && HGHandleHolder.class.isAssignableFrom(javaClass)) return false;
        if (desc.getName().equals("atomType") && HGTypeHolder.class.isAssignableFrom(javaClass)) return false;
        if (desc.getName().equals("hyperGraph") && HGGraphHolder.class.isAssignableFrom(javaClass)) return false;
        Field field = JavaTypeFactory.findDeclaredField(javaClass, desc.getName());
        if (field != null && (field.getAnnotation(HGIgnore.class) != null || (field.getModifiers() & Modifier.TRANSIENT) != 0)) return false;
        return true;
    }
