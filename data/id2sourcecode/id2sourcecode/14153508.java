    private static String readFileContent(File file) {
        FileInputStream res = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int read = 0;
        try {
            res = new FileInputStream(file);
            while ((read = res.read(buf)) != -1) {
                baos.write(buf, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toString();
    }
