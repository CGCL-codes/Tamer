    private byte[] findClassBytesFromParent(String binaryName) {
        assert binaryName != null;
        String path = toClassFilePath(binaryName);
        InputStream in = parent.getResourceAsStream(path);
        if (in == null) {
            return null;
        }
        try {
            ByteArrayOutputStream results = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int read = in.read(buf);
                if (read == -1) {
                    break;
                }
                results.write(buf, 0, read);
            }
            return results.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
