    public EntityMetadataBuilder addProperty(final String name, final Class<?> clazz, final boolean nullable, final Method readMethod, final Method writeMethod) {
        m_propertyMetadata.add(new PropertyMetadataImpl(name, clazz, nullable, readMethod, writeMethod));
        return this;
    }
