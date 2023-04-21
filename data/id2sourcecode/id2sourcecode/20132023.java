    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return ((readMethod != null && readMethod.getMember().isAnnotationPresent(annotationClass)) || (writeMethod != null && writeMethod.getMember().isAnnotationPresent(annotationClass)));
    }
