    public IGrammarPartToken createFor(ClassProperty property) {
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
            IGrammarPartToken definition = null;
            if (concrete.isPrimitive() || ReflectionUtil.isBoxedType(concrete) || String.class == concrete || Date.class == concrete || Object.class == concrete || concrete.isEnum()) {
                definition = getSingle(tc, minimumOccurence, maximumOccurence);
                definition.getMetaData().put("property", property);
                definition.getMetaData().put("type", concrete);
            } else {
                TokenCompositeConfiguration tcc = TokenCompositeConfiguration.getFor(concrete, concrete, tc, BasicLogic.Sequential);
                TokenConfiguration pc = TokenConfiguration.getFor(property.getJavaMember());
                if (pc.getPrefix().length != 0 || pc.getSuffix().length != 0) {
                    List<IGrammarPartToken> tokens = new ArrayList<IGrammarPartToken>();
                    GrammarTokenReference ref = getReference(concrete, tcc, 1, 1);
                    ref.getMetaData().put("property", property);
                    ref.getMetaData().put("type", concrete);
                    this.appendPrefixes(tokens, pc.getPrefix());
                    tokens.add(ref);
                    this.appendSuffixes(tokens, pc.getSuffix());
                    definition = new GrammarTokenComposite(minimumOccurence, maximumOccurence, tcc.getLogic() == BasicLogic.Random ? OR_LOGIC : AND_LOGIC, tokens.toArray(new IGrammarPartToken[tokens.size()]));
                } else {
                    definition = getReference(concrete, tcc, minimumOccurence, maximumOccurence);
                    definition.getMetaData().put("property", property);
                    definition.getMetaData().put("type", type);
                }
            }
            properties.put(id, definition);
        }
        return properties.get(id);
    }
