    public void add(String name, Class type, boolean readable, boolean writeable) {
        assertNotRestricted();
        DynaProperty dynaProperty = new DynaProperty(name, type);
        dynamicProperties.add(dynaProperty);
    }
