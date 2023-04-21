    private static PropertyAccessor[] createPropertyAccessors(final Class<?> clazz) {
        final List<PropertyAccessor> accessors = new ArrayList<PropertyAccessor>();
        for (Field field : Reflection.getFields(clazz)) {
            if (!Modifier.isStatic(field.getModifiers())) {
                if (field.getAnnotation(Context.class) != null) {
                    Method readMethod = Reflection.findReadMethod(clazz, field, true);
                    Method writeMethod = Reflection.findWriteMethod(clazz, field, true);
                    accessors.add(new PropertyAccessor(readMethod, writeMethod));
                } else {
                    if (!Modifier.isTransient(field.getModifiers())) {
                        throw new NanoConfigurationException(field, "@Context annotation expected");
                    }
                }
            }
        }
        return accessors.toArray(new PropertyAccessor[accessors.size()]);
    }
