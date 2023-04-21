    protected byte[] findClass(String binaryName) {
        InputStream stream = findResourceAsStream(InterceptClassLoader.toClassFilePath(binaryName));
        if (stream == null) {
            return null;
        }
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1000];
            while (true) {
                int read = stream.read(buffer);
                if (read < 0) {
                    break;
                }
                result.write(buffer, 0, read);
            }
            return result.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }
