    private void build(MethodPart read, MethodPart write) throws Exception {
        Annotation label = read.getAnnotation();
        String name = read.getName();
        if (!write.getAnnotation().equals(label)) {
            throw new MethodException("Annotations do not match for '%s' in %s", name, type);
        }
        Class type = read.getType();
        if (type != write.getType()) {
            throw new MethodException("Method types do not match for %s in %s", name, type);
        }
        add(new MethodContact(read, write));
    }
