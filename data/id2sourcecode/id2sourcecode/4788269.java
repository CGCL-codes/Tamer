    public void addModelMBeanAttribute(String fname, String ftype, boolean read, boolean write, boolean is, String description, Descriptor desc) {
        attributes.put(fname, new ModelMBeanAttributeInfo(fname, ftype, description, read, write, is, desc));
    }
