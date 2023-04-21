    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/') + ".class";
        Class<?> clazz = null;
        InputStream in = new BufferedInputStream(getResourceAsStream(path));
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            for (int n; (n = in.read(bytes)) != -1; baos.write(bytes, 0, n)) {
            }
            bytes = baos.toByteArray();
            clazz = defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
        }
        try {
            in.close();
        } catch (Exception e) {
        }
        if (clazz != null) {
            return clazz;
        }
        return super.findClass(name);
    }
