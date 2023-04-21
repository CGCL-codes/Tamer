    protected IPropertyAccessor createPropertyAccessor() {
        return new MethodAccessor(this, readMethod, writeMethod);
    }
