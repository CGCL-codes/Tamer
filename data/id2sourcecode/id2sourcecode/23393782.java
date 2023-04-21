    public static byte[] loadFileBytes(String fileName) throws Exception {
        InputStream is = null;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int n = 0;
        try {
            URL url = checkJarFile(fileName);
            if (url != null) {
                is = url.openStream();
                while (-1 != (n = is.read(buffer))) {
                    baos.write(buffer, 0, n);
                }
                return baos.toByteArray();
            }
            File file = checkFsFile(fileName);
            if (file != null) {
                is = new FileInputStream(file);
                while (-1 != (n = is.read(buffer))) {
                    baos.write(buffer, 0, n);
                }
                return baos.toByteArray();
            }
        } catch (IOException e) {
            throw new Exception(e);
        } finally {
            if (is != null) {
                is.close();
                is = null;
            }
        }
        throw new Exception(fileName + "NOT found");
    }
