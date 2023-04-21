    public JavaFieldProperty(JavaTypeFactory provider, Field field, JavaMethod readMethod, JavaMethod writeMethod) {
        super(field);
        this.provider = provider;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }
