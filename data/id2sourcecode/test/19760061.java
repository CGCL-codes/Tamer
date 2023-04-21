    private byte[] getClassData(String name) {
        name = name.replace('.', '/');
        name += ".class";
        try {
            JarEntry classEntry = jar.getJarEntry(name);
            if (classEntry == null) return null;
            InputStream in = jar.getInputStream(classEntry);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[32];
            int read = 1;
            while (read > 0) {
                read = in.read(buffer);
                if (read > 0) out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
