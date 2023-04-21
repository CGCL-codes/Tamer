    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> loadedClass = null;
        String className = name.replace('.', '/') + ".class";
        for (int i = 0; i < jarsList.size(); i++) {
            try {
                JarFile jarFile = new JarFile(jarsList.get(i));
                ZipEntry entry = jarFile.getEntry(className);
                if (entry == null) continue;
                InputStream classStream = jarFile.getInputStream(entry);
                ByteArrayOutputStream outStr = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int read = 0;
                while ((read = classStream.read(data)) >= 0) {
                    outStr.write(data, 0, read);
                }
                byte[] theClass = outStr.toByteArray();
                loadedClass = defineClass(name, theClass, 0, theClass.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (loadedClass == null) {
            throw new ClassNotFoundException("Can't find " + name);
        }
        return loadedClass;
    }
