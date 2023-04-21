    private byte[] loadClassFromPath(String path, String name) {
        File classFile = new File(path, name);
        if (classFile.exists() == true) {
            try {
                FileInputStream in = new FileInputStream(classFile);
                ByteArrayOutputStream out = new ByteArrayOutputStream(5000);
                byte[] buffer = new byte[5000];
                int len = 0;
                while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
                in.close();
                out.close();
                return out.toByteArray();
            } catch (IOException ex) {
            }
        }
        return null;
    }
