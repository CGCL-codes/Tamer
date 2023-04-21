    public ClassProperty(ClassDescriptor descriptor, String name, ClassMethod reader, ClassMethod writer) {
        super(descriptor);
        this.name = name;
        this.reader = reader;
        this.writer = writer;
        this.type = reader != null ? reader.getReturnType() : writer.getParameterTypes()[0];
    }
