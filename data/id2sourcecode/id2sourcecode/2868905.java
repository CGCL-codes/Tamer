    public Object create(ALinker aLinker, Context ctx) throws FactoryException {
        Class<?> clazz = ctx.getSubjClass();
        if (ALinker.class.equals(clazz)) {
            return aLinker;
        }
        ImplementedBy implementedBy = clazz.getAnnotation(ImplementedBy.class);
        if (implementedBy != null) {
            clazz = implementedBy.value();
        }
        try {
            final Constructor[] constructors = clazz.getConstructors();
            for (Constructor constructor : constructors) {
                final Annotation annotation = Annotations.findAnnotation(constructor, Inject.class);
                if (annotation != null) {
                    return DependencyInjector.injectConstructor(aLinker, constructor);
                }
            }
            return clazz.newInstance();
        } catch (Exception e) {
            throw new FactoryException("Failed to create instance of " + clazz, e);
        }
    }
