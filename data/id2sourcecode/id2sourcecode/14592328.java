    public void addJar(JarInputStream stream) {
        byte[] buf = new byte[BUFFER_SIZE];
        int count;
        try {
            while (true) {
                JarEntry entry = stream.getNextJarEntry();
                if (entry == null) break;
                String name = entry.getName();
                int size = (int) entry.getSize();
                ByteArrayOutputStream out = size >= 0 ? new ByteArrayOutputStream(size) : new ByteArrayOutputStream(BUFFER_SIZE);
                while ((count = stream.read(buf)) > -1) out.write(buf, 0, count);
                out.close();
                if (name.endsWith(".class")) {
                    classes.put(getClassName(name), out.toByteArray());
                } else {
                    others.put(name, out.toByteArray());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
