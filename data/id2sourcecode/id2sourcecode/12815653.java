    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (DEBUG) System.out.println("IOCT: Parsing class " + className);
        ClassReader creader = null;
        try {
            creader = new ClassReader(new ByteArrayInputStream(classfileBuffer));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        ConstructorVisitor cv = new ConstructorVisitor();
        ClassAnnotationVisitor cav = new ClassAnnotationVisitor(cv);
        creader.accept(cav, ClassReader.SKIP_DEBUG);
        if (cv.getConstructors().size() > 0) {
            if (DEBUG) System.out.println("IOCT: Enhancing " + className);
            ClassWriter cw = new ClassWriter(0);
            ClassVisitor cvisitor = null;
            if (DEBUG) {
                CheckClassAdapter chk = new CheckClassAdapter(cw);
                cvisitor = chk;
            } else {
                cvisitor = cw;
            }
            ClassConstructorWriter writer = new ClassConstructorWriter(cv.getConstructors(), cvisitor);
            creader.accept(writer, ClassReader.SKIP_DEBUG);
            return cw.toByteArray();
        } else return null;
    }
