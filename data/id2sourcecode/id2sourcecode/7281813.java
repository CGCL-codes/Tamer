    public MethodContact(MethodPart read, MethodPart write) {
        this.label = read.getAnnotation();
        this.write = write.getMethod();
        this.read = read.getMethod();
        this.type = read.getType();
    }
