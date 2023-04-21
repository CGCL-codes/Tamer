    private byte[] loadClassData(String name, JarFile file) throws IOException {
        BufferedInputStream in = new BufferedInputStream(file.getInputStream(file.getEntry(name)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] results = new byte[0];
        if (in != null) {
            while (true) {
                byte[] bytes = new byte[4096];
                int read = in.read(bytes);
                if (read < 0) {
                    break;
                }
                out.write(bytes, 0, read);
            }
            results = out.toByteArray();
        }
        in.close();
        out.close();
        return results;
    }
