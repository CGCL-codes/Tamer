    private ClassMeta readFromResource(boolean readAnnotations, String className, ClassLoader classLoader) throws ClassNotFoundException {
        String resource = className.replace('.', '/') + ".class";
        try {
            final URLClassLoader cl = new URLClassLoader(urls, classLoader);
            URL url = cl.getResource(resource);
            if (url == null) {
                throw new ClassNotFoundException(className);
            }
            InputStream is = url.openStream();
            byte[] classBytes = InputStreamTransform.readBytes(is);
            ClassReader cr = new ClassReader(classBytes);
            ClassMetaReaderVisitor ca = new ClassMetaReaderVisitor(readAnnotations, enhanceContext);
            cr.accept(ca, 0);
            return ca.getClassMeta();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Extra ClassPath URLS: " + Arrays.toString(urls));
            String msg = "Error trying to read the class info for " + resource;
            logger.log(Level.SEVERE, msg, e);
            return null;
        }
    }
