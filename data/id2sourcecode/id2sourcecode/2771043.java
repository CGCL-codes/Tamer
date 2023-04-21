    public IASTPattern createFor(ClassProperty property) {
        String id = property.getDescriptor().getDescribedClass().getName() + "." + property.getName();
        if (!properties.containsKey(id)) {
            Class<?> type = property.getType();
            TokenConfiguration tc = null;
            int minimumOccurence = 0;
            int maximumOccurence = 1;
            Class<?> concrete = type;
            if (Collection.class.isAssignableFrom(type)) {
                concrete = ReflectionUtil.getConcreteGeneric(property.getGenericType(), 0);
                if (concrete == null || concrete == Object.class) {
                    throw new IllegalStateException("Cannot determine concrete token type for: " + property);
                }
                tc = TokenConfiguration.getFor(property, concrete);
                maximumOccurence = 0;
            } else {
                if (!property.isReadable() || !property.isWritable()) throw new IllegalArgumentException("Pojo properties must be read/write: " + property.getJavaMember());
                tc = TokenConfiguration.getFor(property, concrete);
            }
            minimumOccurence = tc.isRequired() ? 1 : 0;
            IASTPattern definition = null;
            if (concrete.isPrimitive() || ReflectionUtil.isBoxedType(concrete) || String.class == concrete || Date.class == concrete || Object.class == concrete || concrete.isEnum()) {
                definition = getSingle(tc, minimumOccurence, maximumOccurence);
                definition.getMetaData().put("property", property);
                definition.getMetaData().put("type", type);
            } else {
                TokenCompositeConfiguration tcc = TokenCompositeConfiguration.getFor(concrete, concrete, tc, CompositeLogic.And);
                definition = getReference(concrete, tcc, minimumOccurence, maximumOccurence);
                definition.getMetaData().put("property", property);
                definition.getMetaData().put("type", type);
                if ((maximumOccurence == 0 || maximumOccurence > 1) && tc.getSeparator() != null) {
                    definition = new ASTPatternComposite(0, 1, 1, SEPARATOR_LOGIC, createPattern(tc.getSeparator(), 0), definition);
                }
            }
            properties.put(id, definition);
        }
        return properties.get(id);
    }
