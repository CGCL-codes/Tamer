    private byte[] loadClassBytes(JarFile jar, String name) {
        String resourceName = name.replace('.', '/') + ".class";
        byte[] classData = null;
        JarEntry entry = (JarEntry) jar.getEntry(resourceName);
        if (entry != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) entry.getSize());
            byte[] buffer = new byte[1024 * 4];
            int numread = 0;
            try {
                InputStream jarFile = jar.getInputStream(entry);
                while ((numread = (jarFile.read(buffer))) > 0) {
                    baos.write(buffer, 0, numread);
                }
                classData = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.error("No entry " + resourceName + " in  " + jar.getName());
        }
        return classData;
    }
