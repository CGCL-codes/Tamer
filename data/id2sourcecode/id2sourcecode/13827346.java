    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(true);
        LocalVariableVisitor varVisitor = new LocalVariableVisitor();
        reader.accept(varVisitor, false);
        reader.accept(new ClassInstrumenter(varVisitor.getVariables(), writer), false);
        return writer.toByteArray();
    }
