    protected Class loadClassFromFile(String name, File f) {
        byte[] bytes = new byte[4096];
        try {
            FileInputStream in = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            int read = 0;
            while (true) {
                read = in.read(bytes);
                if (read == -1) break;
                out.write(bytes, 0, read);
            }
            in.close();
            out.close();
            byte[] result = out.toByteArray();
            return defineClass(name, result, 0, result.length);
        } catch (IOException exc) {
            return null;
        }
    }
