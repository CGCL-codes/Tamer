    public synchronized Class findClass(String className) throws ClassNotFoundException {
        String classPath = className.replace('.', '/') + ".class";
        if (this.index.containsKey(classPath)) {
            return (Class) this.index.get(classPath);
        }
        try {
            JarInputStream in = new JarInputStream(this.classworldsJarUrl.openStream());
            try {
                JarEntry entry = null;
                while ((entry = in.getNextJarEntry()) != null) {
                    if (entry.getName().equals(classPath)) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        try {
                            byte[] buffer = new byte[2048];
                            int read = 0;
                            while (in.available() > 0) {
                                read = in.read(buffer, 0, buffer.length);
                                if (read < 0) {
                                    break;
                                }
                                out.write(buffer, 0, read);
                            }
                            buffer = out.toByteArray();
                            Class cls = defineClass(className, buffer, 0, buffer.length);
                            this.index.put(className, cls);
                            return cls;
                        } finally {
                            out.close();
                        }
                    }
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException("io error reading stream for: " + className);
        }
        throw new ClassNotFoundException(className);
    }
