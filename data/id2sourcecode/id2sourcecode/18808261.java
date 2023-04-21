    private static boolean includeProperty(Class<?> javaClass, PropertyDescriptor desc) {
        Method reader = desc.getReadMethod();
        Method writer = desc.getWriteMethod();
        if (reader == null || writer == null) return false;
        if (reader.getAnnotation(HGIgnore.class) != null || writer.getAnnotation(HGIgnore.class) != null) return false;
        try {
            Field field = javaClass.getDeclaredField(desc.getName());
            return field.getAnnotation(HGIgnore.class) == null && (field.getModifiers() & Modifier.TRANSIENT) == 0;
        } catch (NoSuchFieldException ex) {
            return true;
        }
    }
