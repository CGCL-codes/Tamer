    public MethodContact(MethodPart read, MethodPart write) {
        this.label = read.getAnnotation();
        this.items = read.getDependants();
        this.item = read.getDependant();
        this.write = write.getMethod();
        this.read = read.getMethod();
        this.type = read.getType();
        this.name = read.getName();
    }
