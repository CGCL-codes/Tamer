    protected void addMBeanAttribute(String fname, String ftype, boolean read, boolean write, boolean is, String desc) {
        attributes.put(fname, new MBeanAttributeInfo(fname, ftype, desc, read, write, is));
    }
