    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (loader == null || shouldIgnore(className)) {
            return null;
        }
        final ClassReader reader = new ClassReader(classfileBuffer);
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        reader.accept(new PowerMockClassVisitor(writer), ClassReader.SKIP_FRAMES);
        return writer.toByteArray();
    }
