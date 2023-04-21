    public PropertyMetadataImpl(final String name, final Class<?> propertyClass, final boolean nullable, final Method readMethod, final Method writeMethod) {
        super();
        assert null != name : "The [name] argument cannot be null.";
        assert null != propertyClass : "The [propertyClass] argument cannot be null.";
        assert null != readMethod : "The [readMethod] argument cannot be null.";
        m_name = name;
        m_nullable = nullable;
        m_propertyClass = propertyClass;
        m_readMethod = readMethod;
        m_writeMethod = writeMethod;
    }
