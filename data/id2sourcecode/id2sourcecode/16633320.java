    public Class<?> resolve(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            org.objectweb.asm.ClassReader classReader = new org.objectweb.asm.ClassReader(is);
            ClassAnnotationVisitor visitor = new ClassAnnotationVisitor();
            classReader.accept(visitor, org.objectweb.asm.ClassReader.SKIP_CODE | org.objectweb.asm.ClassReader.SKIP_DEBUG | org.objectweb.asm.ClassReader.SKIP_FRAMES);
            if ((visitor.access & org.objectweb.asm.Opcodes.ACC_PUBLIC) != 0 && visitor.annotations.contains(annotationClassDesc)) return loader.loadClass(visitor.name.replace('/', '.'));
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException ignored) {
            }
        }
        return null;
    }
