    private void print(String className) {
        try {
            InputStream input = getClass().getResourceAsStream("/" + className.replace('.', '/') + ".class");
            System.out.println("Loaded byte code");
            ClassReader reader;
            reader = new ClassReader(input);
            ASMifierClassVisitor writer = new ASMifierClassVisitor(new PrintWriter(System.out));
            reader.accept(writer, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
