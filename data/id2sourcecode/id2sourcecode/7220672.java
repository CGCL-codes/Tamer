    public static boolean isDeprecated(Method read, Method write) {
        return ((read != null && read.getAnnotation(Deprecated.class) != null) || (write != null && write.getAnnotation(Deprecated.class) != null));
    }
