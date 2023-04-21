    private static void _registerDynamicType(@Nonnull final Class<?> aClass, @Nonnull final Class<? extends IDynamicValue> aDynamicValueClass, final boolean bAllowOverwrite) {
        if (aClass == null) throw new NullPointerException("class");
        if (aDynamicValueClass == null) throw new NullPointerException("dynamicTypeClass");
        if (!ClassHelper.isInstancableClass(aDynamicValueClass)) throw new IllegalArgumentException("The passed dynamic type class must be public, instancable and needs a no-argument constructor: " + aDynamicValueClass);
        s_aRWLock.writeLock().lock();
        try {
            if (!bAllowOverwrite && s_aMap.containsKey(aClass)) throw new IllegalArgumentException("A dynamic value class is already registered for " + aClass);
            if (GlobalDebug.isDebugMode()) {
                final Class<?> aNativeClass = GenericReflection.newInstance(aDynamicValueClass).getNativeClass();
                if (!aClass.equals(aNativeClass) && !ClassHelper.getPrimitiveWrapperClass(aClass).equals(aNativeClass)) throw new IllegalArgumentException(aClass + " is different from native class " + aNativeClass);
            }
            s_aMap.put(aClass, aDynamicValueClass);
        } finally {
            s_aRWLock.writeLock().unlock();
        }
    }
