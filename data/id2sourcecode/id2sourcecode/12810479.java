    public MBeanAttribute(MBean parent, String name, String description, String type, boolean readAble, boolean writeable, boolean isIs) {
        super(parent, name, description);
        this.type = type;
        this.readAble = readAble;
        this.writeable = writeable;
        this.isIs = isIs;
    }
