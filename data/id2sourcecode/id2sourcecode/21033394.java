    private void build(MethodPart read, MethodPart write) throws Exception {
        String name = read.getName();
        Class type = read.getType();
        if (type == write.getType()) {
            put(name, new Property(read, write));
        }
    }
