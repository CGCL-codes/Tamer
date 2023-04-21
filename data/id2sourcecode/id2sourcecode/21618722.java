    public PotatoPropertyDescriptor(String propertyName, Method readMethod, Method writeMethod, IPotatoProperty potatoProperty) throws IntrospectionException {
        super(propertyName, readMethod, writeMethod);
        FeatureDescriptorUtil.initFeatureDescriptorFromPotatoElement(this, potatoProperty);
    }
