    public Variable(String name, boolean read, boolean write, boolean required, String mappedName) {
        this.name = name;
        this.read = Boolean.valueOf(read);
        this.write = Boolean.valueOf(write);
        this.required = Boolean.valueOf(required);
        this.mappedName = mappedName;
    }
