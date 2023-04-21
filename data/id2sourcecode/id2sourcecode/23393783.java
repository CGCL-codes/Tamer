    public static String loadFileContent(String fileName) throws Exception {
        StringBuilder buf = null;
        InputStream is = null;
        int i = 0;
        try {
            URL url = checkJarFile(fileName);
            if (url != null) {
                is = url.openStream();
                buf = new StringBuilder();
                while (is.available() != 0 && (i = is.read()) != -1) {
                    buf.append((char) i);
                }
                return buf.toString();
            }
            File file = checkFsFile(fileName);
            if (file != null) {
                is = new FileInputStream(file);
                buf = new StringBuilder();
                while (is.available() != 0 && (i = is.read()) != -1) {
                    buf.append((char) i);
                }
                return buf.toString();
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
